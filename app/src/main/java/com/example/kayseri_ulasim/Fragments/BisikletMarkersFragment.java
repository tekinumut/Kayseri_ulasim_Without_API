package com.example.kayseri_ulasim.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.example.kayseri_ulasim.Adapters.MarkerBisikletAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBisiklet;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BisikletMarkersFragment extends Fragment implements OnMapReadyCallback {
  
  private Context mContext;
  private GoogleMap mMap;
  private MapView mapView;
  private List<ModelBisiklet> modelBisiklets;
  
  
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
		Type type = new TypeToken<List<ModelBisiklet>>() {
		}.getType();
		modelBisiklets = new Gson().fromJson(getValuesFromBundle, type);
	 }
	 
	 return rootView;
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 mMap = googleMap;
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		mMap.setMyLocationEnabled(true);
	 }
	 
	 mMap.setInfoWindowAdapter(new MarkerBisikletAdapter(mContext, mMap));
	 
	 for (int i = 0; i < modelBisiklets.size(); i++) {
		
		double latitude = Double.parseDouble(modelBisiklets.get(i).getLat());
		double longitude = Double.parseDouble(modelBisiklets.get(i).getLng());
		
		int toplamStok = modelBisiklets.get(i).getBosSlot() + modelBisiklets.get(i).getDoluSlot();
		double yuzde = (double) modelBisiklets.get(i).getBosSlot() / (double) toplamStok;
		String snippet = new Gson().toJson(modelBisiklets.get(i));
		
		if (modelBisiklets.get(i).getBosSlot() == toplamStok) {
		  
		  addMarker(new LatLng(latitude, longitude), modelBisiklets.get(i).getDurakAdi(), snippet, R.drawable.ic_bike_red);
		}//Eğer slotların en az %80i dolu ise
		if (yuzde < 1.00 && yuzde >= 0.80) {
		  addMarker(new LatLng(latitude, longitude), modelBisiklets.get(i).getDurakAdi(), snippet, R.drawable.ic_bike_orange);
		}
		//Eğer slotlar %80'den daha az dolu ise
		else if (yuzde >= 0 && yuzde <= 0.80) {
		  addMarker(new LatLng(latitude, longitude), modelBisiklets.get(i).getDurakAdi(), snippet, R.drawable.ic_bike_green);
		}
		
	 }
	 //Kayseri merkez konum
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CallMethods.getCurrentLocation(mContext).latitude,
				CallMethods.getCurrentLocation(mContext).longitude), 14.5f));
	 
	 mMap.setOnInfoWindowClickListener(marker -> {
		
		CallMethods.getChooseDirectionsDialog(mContext, "AlertMarkerInfoPanel", marker.getTitle(), "",
				  marker.getPosition().latitude, marker.getPosition().longitude);
		//
		//
	 });
	 
  }
  
  private void addMarker(LatLng userLocation, String name, String snippet, int image) {
	 mMap.addMarker(new MarkerOptions()
				.snippet(snippet)
				.position(userLocation)
				.title(name).draggable(false)
				.icon(bitmapDescriptorFromVector(mContext, image)));
  }
  
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
