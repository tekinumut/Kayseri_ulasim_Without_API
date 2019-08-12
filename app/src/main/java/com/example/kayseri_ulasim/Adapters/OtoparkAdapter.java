package com.example.kayseri_ulasim.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelOtopark;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class OtoparkAdapter extends RecyclerView.Adapter<OtoparkAdapter.OtoparkViewHolder> {
  
  private Context mContext;
  private List<ModelOtopark> recyclerList;
  
  public OtoparkAdapter(Context mContext, List<ModelOtopark> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public OtoparkAdapter.OtoparkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 
	 View mView = LayoutInflater.from(mContext).inflate(R.layout.rec_inside_otopark, parent, false);
	 mView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	 
	 return new OtoparkViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull OtoparkAdapter.OtoparkViewHolder holder, int position) {
	 
	 ModelOtopark modelOtopark = recyclerList.get(position);
	 
	 holder.name.setText(modelOtopark.getName());
	 
	 if (modelOtopark.isKatli()) {
		holder.katli.setText("Katlı Otopark");
		holder.katli.setTextColor(ContextCompat.getColor(mContext, R.color.nobetgece1));
	 } else {
		holder.katli.setText("Katsız Otopark");
		holder.katli.setTextColor(ContextCompat.getColor(mContext, R.color.nobet24saat));
	 }
	 
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		//eğer izin alınmışsa işlemleri başlat
		if (modelOtopark.getDistance() != 0.0) {
		  holder.distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(modelOtopark.getDistance()));
		} else {
		  holder.distance.setText("Uzaklık: Konum bilgisine erişilemedi.");
		}
	 } else {
		holder.distance.setText("Uzaklık: Konum bilgisine erişilemedi.");
	 }
	 
	 holder.itemView.setOnClickListener(v -> {
		
		String lat = modelOtopark.getLatitude();
		String lng = modelOtopark.getLongitude();
		
		if (lat.length() != 0 && lng.length() != 0) {
		  Double latitude = Double.parseDouble(lat);
		  Double longitude = Double.parseDouble(lng);
		  if (modelOtopark.isKatli()) {
			 CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", modelOtopark.getName(), "Katlı Otopark",
						latitude, longitude);
		  } else {
			 CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", modelOtopark.getName(), "Katsız Otopark",
						latitude, longitude);
		  }
		} else {
		  Toast.makeText(mContext, "Konum bilgisine erişilemedi", Toast.LENGTH_SHORT).show();
		}
		
	 });
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  
  public class OtoparkViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView name, katli, distance;
	 
	 public OtoparkViewHolder(@NonNull View itemView) {
		super(itemView);
		
		name = itemView.findViewById(R.id.otopark_name);
		katli = itemView.findViewById(R.id.otopark_katli);
		distance = itemView.findViewById(R.id.otopark_distance);
		
	 }
  }
}
