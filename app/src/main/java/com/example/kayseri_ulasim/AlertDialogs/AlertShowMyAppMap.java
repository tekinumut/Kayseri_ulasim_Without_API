package com.example.kayseri_ulasim.AlertDialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AlertShowMyAppMap extends DialogFragment implements OnMapReadyCallback {
  
  private Context mContext;
  private String name;
  private Double latitude, longitude;
  private String snippet;
  private GoogleMap mMap;
  private MapView mapView;
  
  
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
	 
	 View mView = View.inflate(mContext, R.layout.alert_show_map_from_app, null);
	 
	 // Gets the MapView from the XML layout and creates it
	 mapView = mView.findViewById(R.id.mapView);
	 mapView.onCreate(savedInstanceState);
	 
	 mapView.getMapAsync(this);
	 
	 Bundle bundle = getArguments();
	 if (bundle != null) {
		
		snippet = bundle.getString(getString(R.string.bundle_snippet));
		name = bundle.getString(getString(R.string.bundle_name));
		latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		longitude = bundle.getDouble(getString(R.string.bundle_longitude));
	 }
	 
	 AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setView(mView)
				.setNegativeButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());
	 
	 
	 return builder.create();
	 
  }
  
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 mMap = googleMap;
	 
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		mMap.setMyLocationEnabled(true);
	 }
	 
	 LatLng userLocation = new LatLng(latitude, longitude);
	 
	 addMarker(name, snippet, userLocation);
	 
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.5f));
	 
  }
  
  private void addMarker(String name, String snippet, LatLng userLocation) {
	 
	 if (snippet.isEmpty()) {
		mMap.addMarker(new MarkerOptions()
				  .position(userLocation)
				  .title(name).draggable(false)
				  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	 } else {
		//Haritanın odaklandığı yere marker ekle
		mMap.addMarker(new MarkerOptions()
				  .position(userLocation)
				  .snippet(snippet)
				  .title(name).draggable(false)
				  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	 }
	 
	 
  }
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
  public void onStart() {
	 super.onStart();
	 if (getDialog() != null)
		((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
	 
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
