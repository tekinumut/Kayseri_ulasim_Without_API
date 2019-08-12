package com.example.kayseri_ulasim.Fragments.Stations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.StationsAdapter;
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.Model.ModelStationsResponse;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL;
import com.example.kayseri_ulasim.util.Constants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tab1StationsPager extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
  
  private Context mContext;
  private Double latitude, longitude;
  private List<ModelStationList> stationLists;
  private SearchView searchStations;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout refreshLayout;
  private FavoritesRoomDatabase favoritesDatabase;
  private View rootView;
  private ProgressBar progressBar;
  private IntentIntegrator integrator;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 rootView = inflater.inflate(R.layout.tab1_stations_list, container, false);
	 
	 setupRecyclerView();
	 
	 searchStations = rootView.findViewById(R.id.searchStations);
	 progressBar = rootView.findViewById(R.id.progressBar_tab1);
	 //  integrator = new IntentIntegrator(getActivity());//Barkod sınıfını tanımladık. Activity için
	 integrator = IntentIntegrator.forSupportFragment(this);
	 
	 Bundle bundle = getArguments();
	 if (bundle != null) {
		//StationsFragment'dan gelen real-time enlem ve boylam değerleri burada alınır.
		latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		longitude = bundle.getDouble(getString(R.string.bundle_longitude));
		
		FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().getLocation().observe(this, model -> {
		  
		  if (model.size() > 0) {
			 latitude = model.get(0).getLatitude();
			 longitude = model.get(0).getLongitude();
		  }
		});
		
		//En başta bize en yakın 20 durak listelenir.
		fetchNearbyStations();
		
	 }
	 
	 favoritesDatabase = Room.databaseBuilder(mContext, FavoritesRoomDatabase.class, mContext.getString(R.string.favorites_database_name))
				.allowMainThreadQueries()
				.build();
	 
	 
	 setFavoriteDistance();
	 
	 favoriteOperation();
	 
	 setSearchViewListener();
	 
	 barcodeButton();
	 
	 return rootView;
  }
  
  private void setupRecyclerView() {
	 recyclerView = rootView.findViewById(R.id.rec_view_stations);
	 refreshLayout = rootView.findViewById(R.id.swipe_rec_view_stations);
	 refreshLayout.setOnRefreshListener(Tab1StationsPager.this);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
	 
	 
  }
  
  //Favori ve tüm liste arasında geçiş yaparken ele alınacak işlemler
  private void favoriteOperation() {
	 TextView favorite_button = rootView.findViewById(R.id.tab1_my_favorites);
	 SharedPreferences amIinFavorites = mContext.getSharedPreferences(getString(R.string.booleanFavorites), Context.MODE_PRIVATE);
	 final SharedPreferences.Editor amIinFavoritesEditor = amIinFavorites.edit();
	 
	 //İlk başta devamlı favori dışında olacağız. Ve tüm liste üzerinden işlemler yapılacak.
	 amIinFavoritesEditor.putBoolean("favorite", false).apply();
	 favorite_button.setOnClickListener(v -> {
		//Favorileri göstermek istediğimde
		if (!CallMethods.imInFavorites(mContext)) {
		  //Favorileri göster dediğimde , tüm favorilerimi listele.
		  favorite_button.setText(getString(R.string.show_all));
		  recyclerView.setAdapter(null);
		  recyclerView.setAdapter(new StationsAdapter(mContext, favoritesDatabase.myFavoritesDao().getRecords()));
		  amIinFavoritesEditor.putBoolean("favorite", true).apply();
		  //Eğer favori kayıdım yoksa kullanıcıyı bilgilendir.
		  if (favoritesDatabase.myFavoritesDao().getRecords().isEmpty()) {
			 CallMethods.createSnackBar(rootView, "Favori kayıt bulunamadı", 2).show();
		  }
		  //Butona tıklayınca focus sil
		  searchStations.clearFocus();
		}
		//Favorilerden çıkış yaptığımda
		else if (CallMethods.imInFavorites(mContext)) {
		  //Favorilerimden çıkış yaptığımda, tüm listeden en yakın 20 durağı getir.
		  favorite_button.setText(getString(R.string.show_myFavs));
		  recyclerView.setAdapter(null);
		  fetchNearbyStations();
		  amIinFavoritesEditor.putBoolean("favorite", false).apply();
		  searchStations.clearFocus();
		}
	 });
  }
  
  //Favorilerim kısmındaki uzaklığın mevcut konuma göre güncellenmesini sağlar
  private void setFavoriteDistance() {
	 List<ModelStationList> getRecords = favoritesDatabase.myFavoritesDao().getRecords();
	 
	 //Sahip olduğumuz tüm favori durakların id'sini alıyoruz ve
	 //Web Service'de IN(1,2,3,4) şeklinde sıralanmasını sağlıyoruz. StringBuilder'in amacı bu.
	 StringBuilder values = new StringBuilder();
	 for (int i = 0; i < getRecords.size(); i++) {
		
		values.append(getRecords.get(i).getId()).append(",");
		
		//döngü sonuna geldiğimizde
		if (i == getRecords.size() - 1) {
		  //substring, sonda kalan ',' işaretini yok etmek için
		  RetroBaseURL.getmyApi().getDistanceOfFav(values.substring(0, values.length() - 1), latitude, longitude).enqueue(new Callback<ModelStationsResponse>() {
			 @Override
			 public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
				
				if (response.body() != null && response.body().getStations() != null) {
				  List<ModelStationList> list = response.body().getStations();
				  if (!response.body().getError_msg().equals("empty")) {
					 
					 //Favorilerimizdeki uzaklık birimini güncelliyoruz
					 for (int j = 0; j < list.size(); j++) {
						stationLists = response.body().getStations();
						favoritesDatabase.myFavoritesDao().updateDistance(list.get(j).getDistance(), list.get(j).getId());
					 }
					 
				  }
				}
			 }
			 
			 @Override
			 public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
				
			 }
		  });
		}
		
	 }
  }
  
  private void barcodeButton() {
	 TextView barcode_button = rootView.findViewById(R.id.tab1_barcode);
	 
	 barcode_button.setOnClickListener(v -> {
		
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
		  //eğer kameraya izin alınmışsa direkt işleme geç
		  integrator.initiateScan();
		  
		} else {
		  //izin alınmamışsa izin iste request permission
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			 requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);
		  }
		  
		}
		searchStations.clearFocus();
		
	 });
  }
  
  private void getValuefromBarcode(int id, Double latitude, Double longitude) {
	 
	 RetroBaseURL.getmyApi().getStationBarcode(id, latitude, longitude).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null && response.body().getStations() != null) {
			 if (response.body().getError_msg().equals("empty")) {
				Toast.makeText(mContext, "Durak sistemde bulunamadı", Toast.LENGTH_SHORT).show();
			 } else {
				stationLists = response.body().getStations();
				recyclerView.setAdapter(new StationsAdapter(mContext, stationLists));
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  CallMethods.createSnackBar(rootView, getString(R.string.noDataTaken), 2).show();
		}
	 });
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	 
	 if (requestCode == Constants.REQUEST_CAMERA) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
		  //Eğer izni sonradan kabul ederse direk verileri yüklemesine izin ver
		  integrator.initiateScan();
		} else {
		  CallMethods.createSnackBar(rootView, "Barkodun okunabilmesi için izin vermeniz gerekmektedir.", 3).show();
		}
	 }
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	 IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
	 if (result != null) {
		if (result.getContents() == null) { //Eğer kameradan çıkarsa
		  Toast.makeText(mContext, "İşlem iptal edildi", Toast.LENGTH_LONG).show();
		} else {//Eğer veri yakalanırsa
		  int id = Integer.parseInt(result.getContents().replaceAll("\\D", ""));
		  getValuefromBarcode(id, latitude, longitude);
		  //   Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
		}
	 } else {
		super.onActivityResult(requestCode, resultCode, data);
	 }
  }
  
  private void setSearchViewListener() {
	 
	 //Klavyede ara butonuna tıkladığımda yapılacak işlemler
	 searchStations.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String text) {
		  //Eğer favorilerin içerisinde değilsem
		  if (!CallMethods.imInFavorites(mContext)) {
			 if (text.equals("")) //Eğer text boş ise
			 {
				//Text boş olursa en yakın 20 durağı listele
				fetchNearbyStations();
			 } else {
				fetchSearchedStations(text, text);
			 }
		  } else {//Eğer favoriler içerisinde arama yapıyorsam
			 fetchFavoriteStations();
		  }
		  return true;
		}
		
		@Override     //Her harf değiştiğinde
		public boolean onQueryTextChange(String text) {
		  
		  //Text boş olduğu zaman onQuery'i çağır.
		  //Bu durumda en yakın istasyonlar listelenir.
		  if (text.equals("")) {
			 this.onQueryTextSubmit("");
		  }
		  return true;
		}
	 });
	 
  }
  
  
  //Girilen Texte göre filtreleme yaparak durakları getiren metot
  //Filtreleme id ve durak adına göre yapılır
  private void fetchSearchedStations(String id, String name) {
	 recyclerView.setAdapter(null);
	 progressBar.setVisibility(View.VISIBLE);
	 
	 RetroBaseURL.getmyApi().getSearchedStations(id, name, latitude, longitude).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("empty")) //Eğer veriler boş ise
			 {
				Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 } else {  //Bir sorun yok ise
				if (response.body().getStations() != null) {
				  stationLists = response.body().getStations();
				  recyclerView.setAdapter(new StationsAdapter(mContext, stationLists));
				  searchStations.clearFocus();
				}
				
			 }
			 
		  }
		  progressBar.setVisibility(View.GONE);
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  CallMethods.createSnackBar(rootView, getString(R.string.noDataTaken), 2).show();
		  progressBar.setVisibility(View.GONE);
		}
	 });
	 
  }
  
  //En yakın 20 durağı getir.
  private void fetchNearbyStations() {
	 
	 RestMethods.fetchNearbyStations(latitude, longitude, 20, (situation, modelStationList) -> {
		
		switch (situation) {
		  case "empty":
			 Toast.makeText(mContext, "Herhangi bir kayıt bulunamadı", Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 recyclerView.setAdapter(null);
			 recyclerView.setAdapter(new StationsAdapter(mContext, modelStationList));
			 break;
		  case "failure":
			 CallMethods.createSnackBar(rootView, getString(R.string.noDataTaken), 2).show();
			 progressBar.setVisibility(View.GONE);
			 break;
		}
		progressBar.setVisibility(View.GONE);
	 });
	 
  }
  
  private void fetchFavoriteStations() {
	 //Favorilerimdeki verilerin liste aktarılmasını ele alan metot
	 //Eğer searchView boş ise
	 if (searchStations.getQuery().equals("")) {
		//Favorilerimdeki 50 kaydı getir.
		recyclerView.setAdapter(new StationsAdapter(mContext, favoritesDatabase.myFavoritesDao().getRecords()));
		progressBar.setVisibility(View.GONE);
		//Eğer searchView herhangi bir kelime içeriyor ise
	 } else {
		//Adapter'ı belirle
		StationsAdapter adapter = new StationsAdapter(mContext, favoritesDatabase.myFavoritesDao()
				  .getSearchedRecords(searchStations.getQuery().toString(), CallMethods.toEnglish(searchStations.getQuery().toString())));
		
		//eğer adapter'da kayıt yok ise uyarı ver
		if (adapter.getItemCount() == 0) {
		  
		  Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
		  //Eğer adapter'da veri var ise recyclerView'a verileri yükle , klavyeyi kapat.
		} else {
		  searchStations.clearFocus();
		  recyclerView.setAdapter(adapter);
		}
	 }
  }
  
  @Override
  public void onRefresh() {
	 progressBar.setVisibility(View.VISIBLE);
	 //Eğer favorilerde değilsem
	 if (!CallMethods.imInFavorites(mContext)) {
		//Textin durumuna göre en yakın durakları getir.
		if (searchStations.getQuery().equals("")) {
		  refreshLayout.postDelayed(this::fetchNearbyStations, 500);
		} else {
		  refreshLayout.postDelayed(() -> {
			 //Eğer searchView boş değilse ilgili kelimeyi arayarak yenile
			 fetchSearchedStations(searchStations.getQuery().toString(), searchStations.getQuery().toString());
		  }, 500);
		  
		}
		
	 }
	 //Eğer favorilerde isem
	 else {
		
		refreshLayout.postDelayed(this::fetchFavoriteStations, 500);
		
	 }
	 refreshLayout.setRefreshing(false);
	 progressBar.setVisibility(View.GONE);
	 
  }
  
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}
