package com.example.kayseri_ulasim.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.MarkerInfoAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBusLocation;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBusLocationResponse;
import com.example.kayseri_ulasim.Model.ModelMarkerInfo;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL2;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopsOfLineFragment extends Fragment implements OnMapReadyCallback {
  
  private Context mContext;
  private View rootView;
  private MapView mapView;
  private GoogleMap mMap;
  private Double myLat, myLng;
  private int bus_line_id, bus_line_kod;
  private String bus_line_name;
  
  @SuppressLint("SetTextI18n")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 rootView = inflater.inflate(R.layout.fragment_stopsofline, container, false);
	 
	 // Gets the MapView from the XML layout and creates it
	 mapView = rootView.findViewById(R.id.mapView_sol);
	 mapView.onCreate(savedInstanceState);
	 mapView.getMapAsync(this);
	 TextView textTitle = rootView.findViewById(R.id.lineText_sol);
	 
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		bus_line_id = bundle.getInt(getString(R.string.bundle_bus_line));
		bus_line_kod = bundle.getInt(getString(R.string.bundle_bus_line_kod));
		bus_line_name = bundle.getString(getString(R.string.bundle_bus_line_name));
	 }
	 
	 //başlığın içeriğini gir
	 textTitle.setText(bus_line_kod + " - " + bus_line_name);
	 
	 return rootView;
	 
  }
  
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
	 mMap = googleMap;
	 
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		
		mMap.setMyLocationEnabled(true);
	 }
	 
	 //enlem ve boylam null dönmesin diye alınan son konumu bu değişkenlere ata
	 myLat = CallMethods.getCurrentLocation(mContext).latitude;
	 myLng = CallMethods.getCurrentLocation(mContext).longitude;
	 
	 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLng), 13.5f));
	 
	 mMap.setInfoWindowAdapter(new MarkerInfoAdapter(mContext, mMap, "mesafe"));
	 
	 
	 getRealtimeBus();
	 
	 
	 RestMethods.fetchStopsOfBusLines(bus_line_id, (situation, modelStopsOfLines) -> {
		
		switch (situation) {
		  case "empty":
			 Toast.makeText(mContext, "Herhangi bir kayıt bulunamadı", Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 
			 for (int i = 0; i < modelStopsOfLines.size(); i++) {
				Double buslat = modelStopsOfLines.get(i).getLat();
				Double buslng = modelStopsOfLines.get(i).getLng();
				ModelMarkerInfo modelMarkerInfo = new ModelMarkerInfo(modelStopsOfLines.get(i).getBusid(), modelStopsOfLines.get(i).getName(),
						  buslat, buslng, distanceFromMe(buslat, buslng), modelStopsOfLines.get(i).getOncekiDurakIleMesafe(), "0");
				String markerInfoString = new Gson().toJson(modelMarkerInfo);
				addMarker(markerInfoString);
				if (i < modelStopsOfLines.size() - 1)
				  drawPolyline(new LatLng(buslat, buslng), new LatLng(modelStopsOfLines.get(i + 1).getLat(), modelStopsOfLines.get(i + 1).getLng()));
			 }
			 break;
		  case "failure":
			 CallMethods.createSnackBar(rootView, getString(R.string.noDataTaken), 2).show();
			 break;
		}
		
	 });
	 
	 mMap.setOnMarkerClickListener(marker -> {
		
		marker.showInfoWindow();
		
		return true;
	 });
	 
	 
  }
  
  private void getRealtimeBus() {
	 RetroBaseURL2.getmyApi().getOtobusKonum(bus_line_id, CallMethods.getTokenValue(mContext)).enqueue(new Callback<ModelBusLocationResponse>() {
		@Override
		public void onResponse(Call<ModelBusLocationResponse> call, Response<ModelBusLocationResponse> response) {
		  
		  if (response.body() != null && response.body().getOtobusKonumResponse().getOtobusKonum() != null) {
			 List<ModelBusLocation> otobusKonums = response.body().getOtobusKonumResponse().getOtobusKonum();
			 
			 for (int i = 0; i < otobusKonums.size(); i++) {
				
				ModelMarkerInfo modelMarkerInfo = new ModelMarkerInfo(0, null, null, null, null, 0, "realtime");
				String markerInfoString = new Gson().toJson(modelMarkerInfo);
				
				mMap.addMarker(new MarkerOptions()
						  .position(new LatLng(otobusKonums.get(i).getLat(), otobusKonums.get(i).getLng()))
						  .title(otobusKonums.get(i).getBilgi())
						  .snippet(markerInfoString)
						  .icon(BitmapDescriptorFactory.fromBitmap(turnPicToBitmap(R.drawable.bus_realtime, 80, 80))));
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelBusLocationResponse> call, Throwable t) {
		
		}
	 });
	 
	 
  }
  
  private void addMarker(String json) {
	 
	 ModelMarkerInfo model = new Gson().fromJson(json, ModelMarkerInfo.class);
	 
	 BitmapDescriptor icon;
	 if (FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().isItFav(model.getBusid()) == 1) {
		icon = BitmapDescriptorFactory.fromBitmap(turnPicToBitmap(R.drawable.star_on, 50, 50));
	 } else {
		icon = BitmapDescriptorFactory.fromBitmap(turnPicToBitmap(R.drawable.bus_logo, 50, 50));
	 }
	 
	 LatLng userLocation = new LatLng(model.getLat(), model.getLng());
	 
	 //Haritanın odaklandığı yere marker ekle
	 mMap.addMarker(new MarkerOptions()
				.position(userLocation)
				.snippet(json)
				.title(model.getName()).draggable(false)
				.icon(icon));
	 
  }
  
  private void drawPolyline(LatLng start, LatLng end) {
	 
	 Polyline polyline = mMap.addPolyline(new PolylineOptions()
				.add(start, end)
				.width(7)
				.color(ContextCompat.getColor(mContext, R.color.colorPrimary)));
	 
	 polyline.setJointType(JointType.ROUND);
	 polyline.setEndCap(new CustomCap(BitmapDescriptorFactory.fromBitmap(turnPicToBitmap(R.drawable.up_arrow, 70, 70)), 7));
	 
	 
  }
  
  private Bitmap turnPicToBitmap(int pic, int width, int height) {
	 
	 BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(mContext, pic);
	 assert bitmapdraw != null; //?
	 Bitmap b = bitmapdraw.getBitmap();
	 return Bitmap.createScaledBitmap(b, width, height, false);
  }
  
  private Double distanceFromMe(Double lat2, Double long2) {
	 
	 Location loc1 = new Location("");
	 loc1.setLatitude(myLat);
	 loc1.setLongitude(myLng);
	 
	 Location loc2 = new Location("");
	 loc2.setLatitude(lat2);
	 loc2.setLongitude(long2);
	 
	 
	 Float distance = loc1.distanceTo(loc2);
	 
	 return distance.doubleValue();
	 
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
