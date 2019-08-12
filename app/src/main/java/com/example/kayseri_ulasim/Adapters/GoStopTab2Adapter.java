package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GoStopTab2Adapter extends RecyclerView.Adapter<GoStopTab2Adapter.GoStopViewHolder> {
  
  private Context mContext;
  private List<ModelStationList> recyclerList;
  private GoogleMap mMap;
  
  public GoStopTab2Adapter(Context mContext, List<ModelStationList> recyclerList, GoogleMap mMap) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
	 this.mMap = mMap;
  }
  
  @NonNull
  @Override
  public GoStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 View mView = View.inflate(mContext, R.layout.rec_inside_tab2, null);
	 
	 return new GoStopTab2Adapter.GoStopViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull GoStopViewHolder holder, int position) {
	 ModelStationList model = recyclerList.get(position);
	 
	 holder.station_name.setText("(Durak No: " + model.getId() + ") - " + model.getName());
	 
	 holder.station_distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(model.getDistance()));
	 
	 holder.get_direction_tab2.setOnClickListener(v -> {
	   //haritada ilgili durağa git
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(model.getLatitude(), model.getLongitude()), 14.5f));
	 });
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  
  public class GoStopViewHolder extends RecyclerView.ViewHolder {
	 final TextView station_name, station_distance, get_direction_tab2;
	 
	 public GoStopViewHolder(@NonNull View itemView) {
		super(itemView);
		station_name = itemView.findViewById(R.id.station_name_tab2);
		station_distance = itemView.findViewById(R.id.station_distance_tab2);
		get_direction_tab2 = itemView.findViewById(R.id.get_direction_tab2);
	 }
  }
}
