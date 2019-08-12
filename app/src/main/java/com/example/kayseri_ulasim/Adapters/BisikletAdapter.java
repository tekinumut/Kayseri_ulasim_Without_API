package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBisiklet;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class BisikletAdapter extends RecyclerView.Adapter<BisikletAdapter.BisikletViewHolder> {
  
  private Context mContext;
  private List<ModelBisiklet> recyclerList;
  
  public BisikletAdapter(Context mContext, List<ModelBisiklet> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  
  @NonNull
  @Override
  public BisikletAdapter.BisikletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 
	 View mView = LayoutInflater.from(mContext).inflate(R.layout.rec_inside_bisiklet, parent, false);
	 mView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	 
	 return new BisikletViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull BisikletAdapter.BisikletViewHolder holder, int position) {
	 ModelBisiklet model = recyclerList.get(position);
	 
	 int toplamStok = model.getBosSlot() + model.getDoluSlot();
	 double yuzde = (double) model.getBosSlot() / (double) toplamStok;
	 
	 holder.name.setText(model.getDurakAdi());
	 
	 holder.doluluk.setText(model.getDoluSlot() + " / " + toplamStok);
	 //Eğer slotlar dolu ise
	 if (model.getBosSlot() == toplamStok) {
		holder.doluluk.setTextColor(ContextCompat.getColor(mContext, R.color.red));
	 }//Eğer slotların en az %80i dolu ise
	 if (yuzde < 1.00 && yuzde >= 0.80) {
		holder.doluluk.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
	 }
	 //Eğer slotlar %80'den daha az dolu ise
	 else if (yuzde >= 0 && yuzde <= 0.80) {
		holder.doluluk.setTextColor(ContextCompat.getColor(mContext, R.color.darkgreen));
	 }
	 holder.distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(model.getDistance()));
	 
	 
	 holder.itemView.setOnClickListener(v -> {
		
		String lat = model.getLat();
		String lng = model.getLng();
		
		if (lat.length() > 0 && lng.length() > 0) {
		  Double latitude = Double.parseDouble(lat);
		  Double longitude = Double.parseDouble(lng);
		  
		  CallMethods.getChooseDirectionsDialog(mContext, "BisikletAdapter", model.getDurakAdi(), "",
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
  
  public class BisikletViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView name, doluluk, distance;
	 
	 public BisikletViewHolder(@NonNull View itemView) {
		super(itemView);
		
		name = itemView.findViewById(R.id.bisiklet_name);
		doluluk = itemView.findViewById(R.id.bisiklet_doluluk);
		distance = itemView.findViewById(R.id.bisiklet_distance);
	 }
  }
}
