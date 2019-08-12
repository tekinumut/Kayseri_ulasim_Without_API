package com.example.kayseri_ulasim.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelEczane;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class EczaneAdapter extends RecyclerView.Adapter<EczaneAdapter.EczaneViewHolder> {
  
  private Context mContext;
  private List<ModelEczane> recyclerList;
  
  public EczaneAdapter(Context mContext, List<ModelEczane> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public EczaneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 
	 View mView = View.inflate(mContext, R.layout.rec_inside_eczane, null);
	 
	 return new EczaneViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull EczaneViewHolder holder, int position) {
	 ModelEczane modelEczane = recyclerList.get(position);
	 
	 holder.name.setText(modelEczane.getName());
	 holder.nobet_turu.setText(modelEczane.getNobet_turu());
	 
	 if (modelEczane.getNobet_turu().equals(mContext.getString(R.string.nobet24saat))) {
		holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.nobet24saat));
		holder.nobet_turu.setTextColor(ContextCompat.getColor(mContext, R.color.nobet24saat));
	 } else {
		holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.nobetgece1));
		holder.nobet_turu.setTextColor(ContextCompat.getColor(mContext, R.color.nobetgece1));
	 }
	 
	 holder.adres.setText(modelEczane.getAdres());
	 holder.phone_number.setText(modelEczane.getPhone_number());
	 holder.ilce.setText(modelEczane.getIlce());
	 
	 holder.ara.setOnClickListener(v -> {
		//arama gönder. Direk aramak istersen manifestten izin al
		mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", modelEczane.getPhone_number(), null)));
	 });
	 
	 String lat = modelEczane.getLatitude();
	 String lng = modelEczane.getLongitude();
	 
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		//eğer izin alınmışsa işlemleri başlat
		
		if (modelEczane.getDistance() != 0.0) {
		  holder.distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(modelEczane.getDistance()));
		} else {
		  holder.distance.setText("Konum bilgisine erişilemedi.");
		}
		
	 } else {
		holder.distance.setText("Konum bilgisine erişilemedi.");
	 }
	 
	 holder.yol_tarifi.setOnClickListener(v -> {
		
		if (lat.length() != 0 && lng.length() != 0) {
		  Double latitude = Double.parseDouble(lat);
		  Double longitude = Double.parseDouble(lng);
		  CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", modelEczane.getName(), "",
					 latitude, longitude);
		} else {
		  Toast.makeText(mContext, "Konum bilgisine erişilemedi", Toast.LENGTH_SHORT).show();
		}
	 });
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  
  public class EczaneViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView name, nobet_turu, adres, phone_number, ilce, distance;
	 private Button ara, yol_tarifi;
	 
	 public EczaneViewHolder(@NonNull View itemView) {
		super(itemView);
		
		name = itemView.findViewById(R.id.eczane_name);
		nobet_turu = itemView.findViewById(R.id.eczane_nobet_turu);
		adres = itemView.findViewById(R.id.eczane_adres);
		phone_number = itemView.findViewById(R.id.eczane_phone_number);
		ilce = itemView.findViewById(R.id.eczane_ilce);
		distance = itemView.findViewById(R.id.eczane_distance);
		ara = itemView.findViewById(R.id.button_eczane_ara);
		yol_tarifi = itemView.findViewById(R.id.button_eczane_yol_Tarifi);
		
	 }
  }
  
  
}
