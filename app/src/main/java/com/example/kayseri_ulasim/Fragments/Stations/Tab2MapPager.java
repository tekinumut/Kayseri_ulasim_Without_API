package com.example.kayseri_ulasim.Fragments.Stations;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Adapters.GoStopTab2Adapter;
import com.example.kayseri_ulasim.Adapters.MarkerInfoAdapter;
import com.example.kayseri_ulasim.Model.ModelMarkerInfo;
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.Model.ModelStationsResponse;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Tab2MapPager extends Fragment implements OnMapReadyCallback {
  
  private Context mContext;
  private GoogleMap mMap;
  private Double latitude, longitude;
  private LatLngBounds curScreen;
  private MapView mapView;
  private List<ModelStationList> stationLists, stationLists2;
  private Button make_FullScreen;
  private SearchView searchView;
  private RecyclerView recyclerView;
  private boolean isFull = false;
  private Spinner spinner;
  private View rootView;
  private SharedPreferences preferences;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 rootView = inflater.inflate(R.layout.tab2_stations_map, container, false);
	 
	 // Gets the MapView from the XML layout and creates it
	 mapView = rootView.findViewById(R.id.mapViewTab2);
	 mapView.onCreate(savedInstanceState);
	 mapView.getMapAsync(this);
	 
	 initializeWidgets();
	 
	 
	 Bundle bundle = getArguments();
	 if (bundle != null) {
		latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		longitude = bundle.getDouble(getString(R.string.bundle_longitude));
	 }
	 
	 setSearchViewListener();
	 
	 
	 searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
		if (hasFocus) {
		  setLayoutHeight(3);
		} else {
		  setLayoutHeight(2);
		}
	 });
	 
	 
	 return rootView;
  }
  
  private void initializeWidgets() {
	 make_FullScreen = rootView.findViewById(R.id.make_fullscreen);
	 spinner = rootView.findViewById(R.id.spin_map_type);
	 searchView = rootView.findViewById(R.id.searchView_tab2);
	 
	 recyclerView = rootView.findViewById(R.id.recycler_tab2);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
	 
	 preferences = mContext.getSharedPreferences("heightOfTab2", Context.MODE_PRIVATE);
	 
	 int value = preferences.getInt("value", 1);
	 //Eğer kullanıcı yazı yazma yerine tıkladıktan sonra ekranı 3'e bölerse
	 //Tekrar o kadar küçük map getirme. Standart olarak 2 getir.
	 if (value == 3) {
		setLayoutHeight(2);
	 } else {
		setLayoutHeight(preferences.getInt("value", 2));
	 }
	 
	 
  }
  
  private void setLayoutHeight(int value) {
	 SharedPreferences.Editor prefEditor = preferences.edit();
	 prefEditor.putInt("value", value).apply();
	 DisplayMetrics displaymetrics = new DisplayMetrics();
	 getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	 int height = displaymetrics.heightPixels;
	 
	 ViewGroup.LayoutParams params = mapView.getLayoutParams();
	 params.height = height / (value);
	 mapView.setLayoutParams(params);
  }
  
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 mMap = googleMap;
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		mMap.setMyLocationEnabled(true);
	 }
	 //mMap.getUiSettings().setZoomControlsEnabled(true);
	 
	 //Haritanın gösterim şeklini spinner yardımıyla seç
	 setMapType();
	 
	 
	 make_FullScreen.setOnClickListener(v -> {
		
		if (isFull) {
		  setLayoutHeight(2);
		  isFull = false;
		} else {
		  setLayoutHeight(1);
		  isFull = true;
		}
		
	 });
	 
	 //Girilen konuma haritayı taşı ve zoom yap
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.5f));
	 
	 //     mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Buradasınız").draggable(false)
	 //           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
	 
	 //Kamera her hareket ettikten sonra listener'daki işlemleri yap.
	 mMap.setOnCameraIdleListener(() -> {
		
		mMap.clear(); //haritadaki verileri temizle. Böylece her map yenilendiğinde veriler sıfırdan oluşturulacak
		
		//marker'da gösterilecek layoutu belirle. Burada dialog çıkartıyoruz.
		mMap.setInfoWindowAdapter(new MarkerInfoAdapter(mContext, mMap, "non"));
		//Haritanın köşe noktalarını almak için sınıf
		//Daha sonra bu sınıf ile ekran içerisinde kalan durakları çekeceğiz.
		curScreen = mMap.getProjection().getVisibleRegion().latLngBounds;
		//Haritanın orta noktasını alıyoruz. Bunun sebebi haritada önce haritanın ortasına yakın durakları sıralayacağız.
		LatLng centerofMap = mMap.getCameraPosition().target;
		
		RetroBaseURL.getmyApi().getStationsOnMap(latitude, longitude, centerofMap.latitude, centerofMap.longitude,
				  curScreen.southwest.latitude, curScreen.northeast.latitude,
				  curScreen.southwest.longitude, curScreen.northeast.longitude).enqueue(new Callback<ModelStationsResponse>() {
		  @Override
		  public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
			 
			 if (response.body() != null) {
				if (response.body().getError_msg().equals("empty")) {
				  //do nothing
				  Toast.makeText(mContext, "İlgili alanda durak bulunamadı!", Toast.LENGTH_SHORT).show();
				} else {
				  if (response.body().getStations() != null) {
					 stationLists = response.body().getStations();
					 //Eğer kullanıcı herhangi bir arama yapmamış ise durakları recyclerView'a bas.
					 if (searchView.getQuery().toString().isEmpty()) {
						recyclerView.setAdapter(new GoStopTab2Adapter(mContext, stationLists, mMap));
					 }
					 for (int i = 0; i < stationLists.size(); i++) {
						ModelMarkerInfo modelMarkerInfo = new ModelMarkerInfo(stationLists.get(i).getId(), stationLists.get(i).getName(),
								  stationLists.get(i).getLatitude(), stationLists.get(i).getLongitude(), stationLists.get(i).getDistance(), 0, "0");
						String markerInfoString = new Gson().toJson(modelMarkerInfo);
						addMarker(markerInfoString);
					 }
				  }
				  
				}
				
			 }
			 
		  }
		  
		  @Override
		  public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
			 Toast.makeText(mContext, "Veriler alınırken bir hata meydana geldi", Toast.LENGTH_SHORT).show();
		  }
		});
		
		
	 });
	 
	 
	 mMap.setOnMarkerClickListener(marker -> {
		
		marker.showInfoWindow();
		
		return true;
	 });
	 
  }
  
  private void addMarker(String json) {
	 
	 ModelMarkerInfo model = new Gson().fromJson(json, ModelMarkerInfo.class);
	 
	 BitmapDescriptor icon;
	 if (FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().isItFav(model.getBusid()) == 1) {
		icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.star_on));
	 } else {
		icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.bus_logo));
	 }
	 
	 LatLng userLocation = new LatLng(model.getLat(), model.getLng());
	 
	 //Haritanın odaklandığı yere marker ekle
	 mMap.addMarker(new MarkerOptions()
				.position(userLocation)
				.snippet(json)
				.title(model.getName()).draggable(false)
				.icon(icon));
	 
  }
  
  private Bitmap resizeMapIcons(int icon) {
	 
	 BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(mContext, icon);
	 assert bitmapdraw != null; //?
	 Bitmap b = bitmapdraw.getBitmap();
	 return Bitmap.createScaledBitmap(b, 50, 50, false);
  }
  
  
  private void setSearchViewListener() {
	 
	 //Klavyede ara butonuna tıkladığımda yapılacak işlemler
	 searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String text) {
		  
		  //Text boş olursa tüm hatları tekrar listele
		  if (text.isEmpty()) {
			 //Eğer arama kısmı boş olursa haritadaki durakları koy
			 recyclerView.setAdapter(new GoStopTab2Adapter(mContext, stationLists, mMap));
			 searchView.clearFocus();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedStations(text, text);
			 searchView.clearFocus();
		  }
		  return true;
		}
		
		@Override     //Her harf değiştiğinde
		public boolean onQueryTextChange(String newText) {
		  
		  //Text boş olursa tüm hatları tekrar listele
		  if (newText.isEmpty()) {
			 recyclerView.setAdapter(new GoStopTab2Adapter(mContext, stationLists, mMap));
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedStations(newText, newText);
		  }
		  
		  return true;
		}
	 });
	 
  }
  
  //Girilen Texte göre filtreleme yaparak durakları getiren metot
  //Filtreleme id ve durak adına göre yapılır
  private void fetchSearchedStations(String id, String name) {
	 recyclerView.setAdapter(null);
	 
	 RetroBaseURL.getmyApi().getSearchedStations(id, name, latitude, longitude).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null) {
			 recyclerView.setAdapter(null);
			 if (response.body().getError_msg().equals("empty")) //Eğer veriler boş ise
			 {
				Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 } else {  //Bir sorun yok ise
				recyclerView.setAdapter(null);
				stationLists2 = response.body().getStations();
				recyclerView.setAdapter(new GoStopTab2Adapter(mContext, stationLists2, mMap));
				
			 }
			 
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
		}
	 });
	 
  }
  
  
  private void setMapType() {
	 //Create Shared Preferences
	 final SharedPreferences pref_map_type = mContext.getSharedPreferences("about_map_type", Context.MODE_PRIVATE);
	 
	 /*Create Spinner
	  *Spinner'ın mevcut değeri kayıt ediliyor.
	  * Uygulama yeniden açıldığında spinner mevcut değerini koruyor ve o değerden açılıyor.
	  * Aynı şekilde google map'in türüde son haliyle açılıyor.
	  */
	 
	 String[] arrayItems = {"Normal", "Uydu", "Uydu-Sade"};
	 final int[] actualValues = {1, 4, 2};
	 
	 ArrayAdapter<String> SpinerAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, arrayItems);
	 spinner.setAdapter(SpinerAdapter);
	 spinner.setSelection(pref_map_type.getInt("station_map_value", 0));//Listener bu kodla tetiklenmiyor.
	 
	 spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		  mMap.setMapType(actualValues[position]);
		  pref_map_type.edit().putInt("station_map_value", position).apply();
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		
		}
	 });
	 //end of spinner
  }
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
  @Override
  public void onResume() {
	 mapView.onResume();
	 super.onResume();
  }
  
  @Override
  public void onPause() {
	 super.onPause();
	 mapView.onPause();
  }
  
  @Override
  public void onDestroy() {
	 super.onDestroy();
	 mapView.onDestroy();
  }
  
  @Override
  public void onLowMemory() {
	 super.onLowMemory();
	 mapView.onLowMemory();
  }
}
