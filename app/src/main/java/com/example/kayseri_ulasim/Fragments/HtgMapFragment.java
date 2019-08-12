package com.example.kayseri_ulasim.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.HtgMapAdapter;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelHowToGoAll;
import com.example.kayseri_ulasim.Model.ModelHtgMap;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HtgMapFragment extends Fragment implements OnMapReadyCallback {
  
  private View mView;
  private Context mContext;
  private RecyclerView recyclerView;
  private MapView mapView;
  private GoogleMap mMap;
  private SharedPreferences preferences;
  private ModelHowToGoAll model;
  private boolean isFull = false;
  private HashMap<String, ModelHowToGoAll> hashMap;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 mView = inflater.inflate(R.layout.fragment_htg_map, container, false);
	 // Gets the MapView from the XML layout and creates it
	 mapView = mView.findViewById(R.id.mapViewHtg);
	 mapView.onCreate(savedInstanceState);
	 mapView.getMapAsync(this);
	 
	 init();
	 
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		String jsonValue = bundle.getString("jsonHowToGoAll", "");
		hashMap = (HashMap<String, ModelHowToGoAll>) bundle.getSerializable("hashmap");
		model = new Gson().fromJson(jsonValue, new TypeToken<ModelHowToGoAll>() {
		}.getType());
	 }
	 
	 return mView;
	 
  }
  
  private void init() {
	 recyclerView = mView.findViewById(R.id.recycler_htg);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL));
	 //  dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.recyclerdivider));
	 Button fullscreen = mView.findViewById(R.id.make_fullscreen_Htg);
	 
	 preferences = mContext.getSharedPreferences("heightOfHtg", Context.MODE_PRIVATE);
	 
	 setLayoutHeight(preferences.getInt("value", 2));
	 
	 fullscreen.setOnClickListener(v -> {
		
		if (isFull) {
		  setLayoutHeight(2);
		  isFull = false;
		} else {
		  setLayoutHeight(1);
		  isFull = true;
		}
		
	 });
	 
  }
  
  private void setLayoutHeight(int value) {
	 SharedPreferences.Editor prefEditor = preferences.edit();
	 prefEditor.putInt("value", value).apply();
	 DisplayMetrics displaymetrics = new DisplayMetrics();
	 ((MainActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
	 
	 LatLng myLoc = new LatLng(model.getMy_lat(), model.getMy_lng());
	 
	 //Başlangıç noktasına bir marker koyuyoruz.
	 LatLng firstPickedLoc = new LatLng(model.getBegin_loc().latitude, model.getBegin_loc().longitude);
	 addMarker(firstPickedLoc, "Başlangıç Seçim Noktam", R.drawable.walking_24dp);
	 
	 //Girilen konuma haritayı taşı ve zoom yap
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPickedLoc, 17.0f));
	 
	 
	 //İlk durağa bir marker koyuyoruz.
	 LatLng firstStopLoc = new LatLng(model.getHtgList().getBeginLat(), model.getHtgList().getBeginLng());
	 addMarker(firstStopLoc, "Başlangıç Durağı", R.drawable.ic_bus_24dp_primary);
	 
	 //Son durağa bir mar+ker koyuyoruz.
	 LatLng lastStopLoc = new LatLng(model.getHtgList().getEndLat(), model.getHtgList().getEndLng());
	 addMarker(lastStopLoc, "Varış Durağı", R.drawable.ic_bus_24dp_orange);
	 
	 //Varış noktasına bir marker koyuyoruz.
	 LatLng lastPickedLoc = new LatLng(model.getEnd_loc().latitude, model.getEnd_loc().longitude);
	 addMarker(lastPickedLoc, "Varış Seçim Noktam", R.drawable.walking_24dp_orange);
	 
	 
	 //Konumum ile seçilen ilk konum arasına bir noktalı çizgi çek
	 //drawPolyline(myLoc, firstPickedLoc, 1);
	 
	 //Seçilen konum ile ilk durak arasına bir noktalı çizgi çek
	 drawPolyline(firstPickedLoc, firstStopLoc, 1);
	 
	 //ilk durak ile son duraka arasına bir çizgi çekiyoruz.
	 
	 RestMethods.getLinesBetweenStops(model.getHtgList().getHatid(), model.getHtgList().getBeginStop(), model.getHtgList().getEndStop(),
				(situation, linesBetweenStops) -> {
				  for (int i = 0; i < linesBetweenStops.size() - 1; i++) {
					 try {
						LatLng loc1 = new LatLng(linesBetweenStops.get(i).getLat(), linesBetweenStops.get(i).getLng());
						LatLng loc2 = new LatLng(linesBetweenStops.get(i + 1).getLat(), linesBetweenStops.get(i + 1).getLng());
						drawPolyline(loc1, loc2, 0);
					 } catch (Exception ignored) {
						
					 }
				  }
				});
	 
	 //Son durak ile seçilen son konum arasına bir noktalı çizgi çek
	 drawPolyline(lastStopLoc, lastPickedLoc, 1);
	 
	 List<ModelHtgMap> modelList = new ArrayList<>();
	 String newUnderStand = createSecretKey(model.getHtgList().getBeginStop(), model.getHtgList().getEndStop());
	 
	 //Adım 1: Başlangıç noktasından durağa doğru yürüyecek.
	 String title = model.getHtgList().getBeginName() + " durağına ilerleyiniz";
	 String snippet = "Mesafe: " + CallMethods.yourDistanceAsText(model.getIlkDuragaUzaklik());
	 
	 modelList.add(new ModelHtgMap(title, snippet, resizeMapIcons(R.drawable.walking_primary)));
	 
	 //Durak1'den hatta binecek sonra durak2'de inecek
	 StringBuilder snippetBuilder = new StringBuilder();
	 title = model.getHtgList().getBeginName() + " durağından " + model.getHtgList().getHatad() + " hattına bininiz. "
				+ model.getHtgList().getEndName() + " durağında ininiz. ";
	 
	 if (hashMap.containsKey(newUnderStand)) {
		snippetBuilder.append("Alternatif Hatlar:  \n\n");
		for (HashMap.Entry<String, ModelHowToGoAll> entry : hashMap.entrySet()) {
		  String key = entry.getKey();
		  snippetBuilder.append(entry.getValue().getHtgList().getHatad()).append("\n");
		}
	 }
	 
	 
	 modelList.add(new ModelHtgMap(title, snippetBuilder.toString(), resizeMapIcons(R.drawable.bus_main)));
	 
	 //Durak2'de inecek ve hedefe yürüyecek
	 title = "Hedefinize doğru ilerleyiniz";
	 snippet = "Mesafe: " + CallMethods.yourDistanceAsText(model.getSonDuragaUzaklik());
	 
	 modelList.add(new ModelHtgMap(title, snippet, resizeMapIcons(R.drawable.walking_orange)));
	 
	 
	 recyclerView.setAdapter(new HtgMapAdapter(mContext, modelList));
	 
  }
  
  private String createSecretKey(int beginStop, int endStop) {
	 return "" + beginStop + "" + endStop + "" + (beginStop + endStop);
	 
  }
  
  private Bitmap resizeMapIcons(int image) {
	 
	 BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(mContext, image);
	 assert bitmapdraw != null; //?
	 Bitmap b = bitmapdraw.getBitmap();
	 return Bitmap.createScaledBitmap(b, 100, 100, false);
  }
  
  private void addMarker(LatLng userLocation, String title, int image) {
	 
	 // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(turnPicToBitmap(image, 70, 70));
	 
	 //Haritanın odaklandığı yere marker ekle
	 mMap.addMarker(new MarkerOptions()
				.position(userLocation)
				.title(title).draggable(false)
				.icon(bitmapDescriptorFromVector(mContext, image)));
	 
  }
  
  //if type == 0 düz çizgi else nokta boşluklu
  private void drawPolyline(LatLng start, LatLng end, int type) {
	 
	 //PatternItem DOT = new Dot();
	 PatternItem DASH = new Dash(20);
	 PatternItem GAP = new Gap(20);
	 List<PatternItem> patternItems = Arrays.asList(GAP, DASH);
	 
	 PolylineOptions polylineOptions = new PolylineOptions();
	 polylineOptions.add(start, end)
				.width(7)
				.color(ContextCompat.getColor(mContext, R.color.colorPrimary));
	 
	 if (type == 1) {
		polylineOptions.pattern(patternItems);
	 }
	 
	 mMap.addPolyline(polylineOptions);
	 
  }
  
  //Vector resmi bitmap'e çevirir
  private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
	 Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
	 vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
	 Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
	 Canvas canvas = new Canvas(bitmap);
	 vectorDrawable.draw(canvas);
	 return BitmapDescriptorFactory.fromBitmap(bitmap);
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
