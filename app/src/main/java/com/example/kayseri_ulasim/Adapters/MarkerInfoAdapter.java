package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.AlertDialogs.AlertMarkerInfoPanel;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelMarkerInfo;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
  
  private final View mWindow;
  private Context mContext;
  private GoogleMap mMap;
  private String ekstra;
  
  @SuppressLint("InflateParams")
  public MarkerInfoAdapter(Context mContext, GoogleMap mMap, String ekstra) {
	 this.mContext = mContext;
	 this.mMap = mMap;
	 this.ekstra = ekstra;
	 mWindow = LayoutInflater.from(mContext).inflate(R.layout.marker_info_page, null);
  }
  
  @SuppressLint("SetTextI18n")
  private void reWindowInfo(final Marker marker, View view_marker) {
	 
	 
	 TextView station_name = view_marker.findViewById(R.id.station_name);
	 TextView station_no = view_marker.findViewById(R.id.station_no);
	 TextView station_distance = view_marker.findViewById(R.id.station_distance);
	 TextView station_mesafe = view_marker.findViewById(R.id.station_mesafe);
	 
	 mMap.setOnInfoWindowClickListener(this);
	 
	 ModelMarkerInfo aModelMarkerInfo = new Gson().fromJson(marker.getSnippet(), ModelMarkerInfo.class);
	 
	 if (aModelMarkerInfo.getItems().equals("realtime")) {
		station_name.setText(marker.getTitle());
		station_no.setVisibility(View.GONE);
		station_distance.setVisibility(View.GONE);
		station_mesafe.setVisibility(View.GONE);
	 } else {
		station_no.setVisibility(View.VISIBLE);
		station_distance.setVisibility(View.VISIBLE);
		station_name.setText(marker.getTitle());
		station_no.setText("Durak No: " + aModelMarkerInfo.getBusid());
		station_distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(aModelMarkerInfo.getDistance()));
		
		//Eğer mesafe layout'undan geliyorsak, mesafe bilgisinide kullanıcıya göster
		if (ekstra.equals("mesafe")) {
		  station_mesafe.setVisibility(View.VISIBLE);
		  station_mesafe.setText("Önceki Durak İle Mesafe: " + aModelMarkerInfo.getMesafe() + " m");
		} else if (ekstra.equals("non")) {
		  station_mesafe.setVisibility(View.GONE);
		}
		
	 }
	 
  }
  
  @Override
  public View getInfoWindow(Marker marker) {
	 
	 return null;
  }
  
  @Override
  public View getInfoContents(Marker marker) {
	 reWindowInfo(marker, mWindow);
	 return mWindow;
  }
  
  @Override
  public void onInfoWindowClick(Marker marker) {
	 ModelMarkerInfo aModelMarkerInfo = new Gson().fromJson(marker.getSnippet(), ModelMarkerInfo.class);
	 
	 if (!aModelMarkerInfo.getItems().equals("realtime")) {
		//Burada alertmarker isimli Alert Dialog açılıyor
		//Ve tab2'de ki map'ten aldığımız verileri bu sınıf aldıktan sonra
		//buradan da açılacak olan Alert Dialog'a yönlendiriyoruz
		FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
		AlertMarkerInfoPanel infoPanel = new AlertMarkerInfoPanel();
		
		//Tek bir veri içerisinde Json ile aldık. Böylece bir çok veriyi tek bir array'de alabiliyoruz.
		
		
		Bundle bundle = new Bundle();
		bundle.putString(mContext.getString(R.string.bundle_name), aModelMarkerInfo.getName());
		bundle.putInt(mContext.getString(R.string.bundle_bus_no), aModelMarkerInfo.getBusid());
		bundle.putDouble(mContext.getString(R.string.bundle_latitude), aModelMarkerInfo.getLat());
		bundle.putDouble(mContext.getString(R.string.bundle_longitude), aModelMarkerInfo.getLng());
		// bundle.putDouble(mContext.getString(R.string.bundle_longitude), marker.getPosition().longitude);
		
		infoPanel.setArguments(bundle);
		
		infoPanel.show(manager, null);
	 }
	 
	 
  }
}
