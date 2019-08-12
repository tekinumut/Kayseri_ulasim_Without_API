package com.example.kayseri_ulasim.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelOtopark;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OtoparkMarkersFragment extends Fragment implements OnMapReadyCallback {
  
  private Context mContext;
  private GoogleMap mMap;
  private MapView mapView;
  private List<ModelOtopark> modelOtoparks;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 View rootView = inflater.inflate(R.layout.fragment_markers, container, false);
	 
	 // Gets the MapView from the XML layout and creates it
	 mapView = rootView.findViewById(R.id.mapView_show_marker);
	 mapView.onCreate(savedInstanceState);
	 mapView.getMapAsync(this);
	 
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		String getValuesFromBundle = bundle.getString("values");
		Type type = new TypeToken<List<ModelOtopark>>() {
		}.getType();
		modelOtoparks = new Gson().fromJson(getValuesFromBundle, type);
	 }
	 
	 return rootView;
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 mMap = googleMap;
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		mMap.setMyLocationEnabled(true);
	 }
	 for (int i = 0; i < modelOtoparks.size(); i++) {
		
		double latitude = Double.parseDouble(modelOtoparks.get(i).getLatitude());
		double longitude = Double.parseDouble(modelOtoparks.get(i).getLongitude());
		
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		  
		  if (modelOtoparks.get(i).isKatli()) {
			 addMarker(new LatLng(latitude, longitude), modelOtoparks.get(i).getName() + " (KATLI)",
						"Uzaklığınız: " + CallMethods.yourDistanceAsText(modelOtoparks.get(i).getDistance()));
		  } else {
			 addMarker(new LatLng(latitude, longitude), modelOtoparks.get(i).getName(),
						"Uzaklığınız: " + CallMethods.yourDistanceAsText(modelOtoparks.get(i).getDistance()));
		  }
		} else {
		  addMarker(new LatLng(latitude, longitude), modelOtoparks.get(i).getName(),
					 "Konuma erişilemedi");
		}
		
		
	 }
	 //Kayseri merkez konum
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.722024, 35.487491), 14.5f));
	 
	 mMap.setOnInfoWindowClickListener(marker -> {
		
		CallMethods.getChooseDirectionsDialog(mContext, "AlertMarkerInfoPanel", marker.getTitle(), "",
				  marker.getPosition().latitude, marker.getPosition().longitude);
		//
		//
	 });
	 
  }
  
  private void addMarker(LatLng userLocation, String name, String snippet) {
	 mMap.addMarker(new MarkerOptions()
				.snippet(snippet)
				.position(userLocation)
				.title(name).draggable(false)
				.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons())));
  }
  
  private Bitmap resizeMapIcons() {
	 
	 BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.otopark_logo);
	 assert bitmapdraw != null; //?
	 Bitmap b = bitmapdraw.getBitmap();
	 return Bitmap.createScaledBitmap(b, 60, 75, false);
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
