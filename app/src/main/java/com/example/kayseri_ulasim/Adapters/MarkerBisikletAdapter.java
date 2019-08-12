package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBisiklet;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

public class MarkerBisikletAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
  
  private View mWindow;
  private Context mContext;
  private GoogleMap mMap;
  
  @SuppressLint("InflateParams")
  public MarkerBisikletAdapter(Context mContext, GoogleMap mMap) {
	 this.mContext = mContext;
	 this.mMap = mMap;
	 mWindow = LayoutInflater.from(mContext).inflate(R.layout.marker_bisiklet_page, null);
  }
  
  @SuppressLint("SetTextI18n")
  private void reWindowInfo(final Marker marker, View view_marker) {
	 TextView name = view_marker.findViewById(R.id.bis_durak_name);
	 TextView bos = view_marker.findViewById(R.id.bos_slot);
	 TextView dolu = view_marker.findViewById(R.id.dolu_slot);
	 TextView distance = view_marker.findViewById(R.id.mesafe);
	 
	 mMap.setOnInfoWindowClickListener(this);
	 
	 ModelBisiklet modelBisiklet = new Gson().fromJson(marker.getSnippet(), ModelBisiklet.class);
	 
	 name.setText(modelBisiklet.getDurakAdi());
	 bos.setText("Dolu Yuva Sayısı: " + modelBisiklet.getBosSlot());
	 dolu.setText("Boş Yuva Sayısı: " + modelBisiklet.getDoluSlot());
	 distance.setText("Uzaklık: " + CallMethods.yourDistanceAsText(modelBisiklet.getDistance()));
	 
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
	 
	 CallMethods.getChooseDirectionsDialog(mContext, "AlertMarkerInfoPanel", marker.getTitle(), "",
				marker.getPosition().latitude, marker.getPosition().longitude);
	 
	 
  }
}
