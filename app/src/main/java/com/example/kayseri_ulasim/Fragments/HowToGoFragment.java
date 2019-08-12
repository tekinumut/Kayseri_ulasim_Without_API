package com.example.kayseri_ulasim.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.droidbyme.dialoglib.DroidDialog;
import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Model.ModelHowToGo;
import com.example.kayseri_ulasim.Model.ModelHowToGoAll;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HowToGoFragment extends Fragment implements OnMapReadyCallback {
  
  private Context mContext;
  private View rootView;
  private MapView mapView;
  private GoogleMap mMap;
  private Button howtogo_button;
  private Double my_lat, my_lng;
  private final String beginKey = "start";
  private final String endKey = "end";
  private final String polylineText = "Polyline";
  private HashMap<String, Marker> hashMapMarker;
  private HashMap<String, Polyline> hashMapPolyLine;
  private LatLng locBegin, locEnd;
  private FragmentManager fragmentManager;
  private List<ModelHowToGoAll> howToGoAlls;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 rootView = inflater.inflate(R.layout.fragment_howtogo, container, false);
	 fragmentManager = getFragmentManager();
	 
	 initializeWidgets(savedInstanceState);
	 
	 make_announcement();
	 
	 return rootView;
  }
  
  private void initializeWidgets(Bundle savedInstanceState) {
	 howtogo_button = rootView.findViewById(R.id.howtogo_button);
	 mapView = rootView.findViewById(R.id.howtogo_mapView);
	 mapView.onCreate(savedInstanceState);
	 mapView.getMapAsync(this);
  }
  
  private void make_announcement() {
	 SharedPreferences prefs = mContext.getSharedPreferences("firstTime2", MODE_PRIVATE);
	 
	 if (prefs.getBoolean("firstrun2", true)) {
		
		new DroidDialog.Builder(mContext)
				  .icon(R.drawable.ic_info)
				  .title("Bölüm Tanıtımı")
				  .content("Başlangıç ve Gidiş noktası seçmek için haritada belirlediğiniz noktaya basılı tutunuz.\n" +
							 "Not: Sadece tek hat üzerinde hesap yapılabildiği için şuan için bu bölüm stabil çalışmamaktadır.")
				  .cancelable(false, false)
				  .positiveButton("Tamam", Dialog::dismiss)
				  .show();
		
		prefs.edit().putBoolean("firstrun2", false).apply();
	 }
  }
  
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 
	 //Mevcut konumuma marker koy.
	 mMap = googleMap;
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		googleMap.setMyLocationEnabled(true);
	 }
	 
	 //enlem ve boylam null dönmesin diye alınan son konumu bu değişkenlere ata
	 my_lat = CallMethods.getCurrentLocation(mContext).latitude;
	 my_lng = CallMethods.getCurrentLocation(mContext).longitude;
	 
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_lat, my_lng), 15.0f));
	 
	 //Konum her değiştiğinde latitude,longitude değişir.
	 FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().getLocation().observe(this, model -> {
		
		if (model.size() > 0) {
		  my_lat = model.get(0).getLatitude();
		  my_lng = model.get(0).getLongitude();
		  
		}
	 });
	 
	 //Haritaya ekleyeceğimiz 2 marker'ı yönetmek için hashMap kullanacağız.
	 //Her key 1 marker'ı temsil etmektedir.
	 hashMapMarker = new HashMap<>();
	 hashMapPolyLine = new HashMap<>();
	 
	 
	 //Haritaya uzun basılı tutunca Başlangıç ve Bitiş noktası seçeceğimiz pencere açılacak
	 mMap.setOnMapLongClickListener(latLng -> {
		
		//Başlangıç ve bitiş noktasının seçileceği Dialog yapısının kodlanması
		final Dialog dialog = new Dialog(mContext, R.style.HowtogoDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_howtogo);
		
		TextView beginText = dialog.findViewById(R.id.begin_location_text);
		TextView endText = dialog.findViewById(R.id.end_location_text);
		
		//Başlangıç Noktası seç butonuna tıklayınca
		beginText.setOnClickListener(v -> {
		  
		  dialogOperations(beginKey, latLng, R.drawable.walking_primary);
		  dialog.dismiss();
		});
		
		//Bitiş noktası seç butonuna tıklayınca
		endText.setOnClickListener(v -> {
		  
		  dialogOperations(endKey, latLng, R.drawable.end_flag_32dp);
		  
		  dialog.dismiss();
		});
		
		dialog.show();
	 });
	 
	 button_click();
  }
  
  
  private void dialogOperations(String key, LatLng latLng, int image) {
	 
	 //Önce eğer marker varsa onu siliyoruz.
	 deleteMarker(key);
	 //Daha sonra seçili konuma ait başlangıç markerı ekliyoruz.
	 //Bu veri aynı zamanda hasmap'e key ile kayıt ediliyor.
	 //Böylece istediğimiz marker'ı yönetebileceğiz.
	 addMarker(latLng.latitude, latLng.longitude, key, image);
	 
	 //Eğer kullanıcı hem başlangıç hem bitiş konumunu belirlediyse
	 //bu konumlar arasına bir tane çizgi çek
	 if (hashMapMarker.size() > 1) {
		//Yeni çizgi geldiğinde önceki çizgiyi silmek için
		removePolyline();
		drawPolyline();
	 }
  }
  
  //İki konum arası düz çizgi çek
  private void drawPolyline() {
	 //PatternItem DOT = new Dot();
	 PatternItem DASH = new Dash(20);
	 PatternItem GAP = new Gap(20);
	 List<PatternItem> patternItems = Arrays.asList(GAP, DASH);
	 
	 Polyline line = mMap.addPolyline(new PolylineOptions()
				.add(hashMapMarker.get(beginKey).getPosition(), hashMapMarker.get(endKey).getPosition())
				.width(5)
				.pattern(patternItems)
				.color(ContextCompat.getColor(mContext, R.color.colorPrimary)));
	 hashMapPolyLine.put(polylineText, line);
	 
  }
  
  
  //İki konum arasına çekilmiş çizgiyi sil
  private void removePolyline() {
	 //Eğer mevcutta çizgi varsa sil
	 if (hashMapPolyLine.size() > 0 && hashMapPolyLine.containsKey(polylineText)) {
		Polyline polyline = hashMapPolyLine.get(polylineText);
		
		if (polyline != null) {
		  polyline.remove();
		}
	 }
  }
  
  //Seçilen konuma marker ekler
  private void addMarker(Double lat, Double lng, String text, int image) {
	 
	 BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(image);
	 
	 Marker marker = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title(text).draggable(false)
				.icon(icon));
	 
	 hashMapMarker.put(text, marker);
	 
  }
  
  
  private void deleteMarker(String key) {
	 
	 //Eğer ekrana 2. kez başlangıç veya bitiş noktası konursa eskisini sil
	 //Başlangıç ve Bitiş ile çakışmaması için o keyin içeride olması lazım
	 if (hashMapMarker.size() > 0 && hashMapMarker.containsKey(key)) {
		Marker marker = hashMapMarker.get(key);
		if (marker != null) {
		  marker.remove();
		}
		hashMapMarker.remove(key);
	 }
	 
  }
  
  //Nasıl giderim butonuna tıklayınca yapılacak işlemler
  private void button_click() {
	 
	 
	 howtogo_button.setOnClickListener(v -> {
		
		howToGoAlls = new ArrayList<>();
		
		try {
		  //Kullanıcın seçti başlangıç ve gidiş noktalarını enlem boylam olarak al.
		  
		  locBegin = hashMapMarker.get(beginKey).getPosition();
		  locEnd = hashMapMarker.get(endKey).getPosition();
		} catch (NullPointerException ignored) {
		  Toast.makeText(mContext, "Başlangıç veya varış noktası eksik", Toast.LENGTH_SHORT).show();
		} finally {
		  //gerek olmayabilir. Zaten hata olmadığında burası dönecek.
		  if (locBegin != null && locEnd != null) {
			 
			 //38.7217, 35.48553, 38.72070, 35.48091,
			 //locBegin.latitude, locBegin.longitude, locEnd.latitude, locEnd.longitude,
			 RestMethods.fetchHowToGo(locBegin.latitude, locBegin.longitude, locEnd.latitude, locEnd.longitude,
						1000, 1000, (situation, modelHowToGos) -> {
						  switch (situation) {
							 case "success":
								for (int i = 0; i < modelHowToGos.size(); i++) {
								  ModelHowToGo modHow = modelHowToGos.get(i);
								  
								  Double konumdanUzaklik = distanceFromMe(my_lat, my_lng, locBegin.latitude, locBegin.longitude);
								  
								  Double ilkDuragaUzakligim = distanceFromMe(locBegin.latitude, locBegin.longitude,
											 modHow.getBeginLat(), modHow.getBeginLng());
								  Double sonDuragaUzakligim = distanceFromMe(modHow.getEndLat(), modHow.getEndLng(),
											 locEnd.latitude, locEnd.longitude);
								  double toplamYurumeMesafesi = konumdanUzaklik + ilkDuragaUzakligim + sonDuragaUzakligim;
								  
								  
								  howToGoAlls.add(new ModelHowToGoAll(modHow, locBegin, locEnd, my_lat, my_lng,
											 konumdanUzaklik, ilkDuragaUzakligim, sonDuragaUzakligim, toplamYurumeMesafesi));
								  
								  if (i == modelHowToGos.size() - 1) {
									 String jsonHowToGoAll = new Gson().toJson(howToGoAlls);
									 
									 HtgAlternativesFragment htgFragment = new HtgAlternativesFragment();
									 Bundle bundle = new Bundle();
									 bundle.putString("jsonHowToGoAll", jsonHowToGoAll);
									 
									 htgFragment.setArguments(bundle);
									 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
									 fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
									 fragmentTransaction.add(R.id.fragment_container, htgFragment).addToBackStack("howtogo")
												.commit();
								  }
								}
								
								break;
							 case "empty":
								Toast.makeText(mContext, "Güzergah Bulunamadı", Toast.LENGTH_SHORT).show();
								break;
							 case "failure":
								Toast.makeText(mContext, "Veriler alınırken bir hata meydana geldi", Toast.LENGTH_SHORT).show();
								break;
						  }
						  
						});
		  }
		}
	 });
  }
  
  private Double distanceFromMe(Double lat1, Double long1, Double lat2, Double long2) {
	 
	 Location loc1 = new Location("");
	 loc1.setLatitude(lat1);
	 loc1.setLongitude(long1);
	 
	 Location loc2 = new Location("");
	 loc2.setLatitude(lat2);
	 loc2.setLongitude(long2);
	 
	 Float distance = loc1.distanceTo(loc2);
	 
	 return distance.doubleValue();
	 /* //İlk durağa uzaklık hesabı: Mevcut konumum ile 1. durak arası mesafe
									 Double ilkDuragaUzakligim = distanceFromMe(my_lat, my_lng,
												modelHowToGos.get(k).getBeginLat(), modelHowToGos.get(k).getBeginLng());
									 //Son durağa uzaklık hesabı: Son durak konumu ile gidiş hedefim arası.
									 Double sonDuragaUzakligim = distanceFromMe(modelHowToGos.get(k).getEndLat(), modelHowToGos.get(k).getEndLng(),
												locEnd.latitude, locEnd.longitude);
									 //Toplam yürüme mesafem bu iki uzaklığın toplamı.
									 double toplamYurumeMesafesi = ilkDuragaUzakligim + sonDuragaUzakligim;
									 
									*/
  }
  
  
  @Override
  public void onAttach(@NonNull Context context) {
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
