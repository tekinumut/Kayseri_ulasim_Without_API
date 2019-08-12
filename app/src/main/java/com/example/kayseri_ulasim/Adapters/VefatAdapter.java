package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelVefat;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class VefatAdapter extends RecyclerView.Adapter<VefatAdapter.VefatViewHolder> {
  
  private Context mContext;
  private List<ModelVefat> recyclerList;
  
  public VefatAdapter(Context mContext, List<ModelVefat> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public VefatAdapter.VefatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 View mView = View.inflate(mContext, R.layout.rec_inside_vefat, null);
	 
	 return new VefatViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull VefatAdapter.VefatViewHolder holder, int position) {
	 
	 ModelVefat modelVefat = recyclerList.get(position);
	 
	 holder._name.setText(modelVefat.getName());
	 holder._ana_baba.setText(CallMethods.spanColorBlackText("Ana / Baba Adı: " +
				modelVefat.getBaba_adi() + " / " + modelVefat.getAna_adi(), 0, 15));
	 holder._dogum_yeri_yil.setText(CallMethods.spanColorBlackText("Doğum Yeri / Yılı: " +
				modelVefat.getDogum_yeri() + " / " + modelVefat.getDogum_yili(), 0, 18));
	 holder._mezarlik.setText(CallMethods.spanColorBlackText("Mezarlık: " + modelVefat.getMezarlik(), 0, 9));
	 holder._yer_vakit.setText(CallMethods.spanColorBlackText("Namaz Yeri / Vakit: " +
				modelVefat.getNamaz_yeri() + " " + modelVefat.getNamaz_vakti(), 0, 19));
	 holder._taziye_adresi.setText(CallMethods.spanColorBlackText("Taziye Adresi: " + modelVefat.getTaziye_adresi(), 0, 13));
	 
	 holder.button_taziye_adresi.setOnClickListener(v -> {
		String lat = modelVefat.getTaziye_konum().getLatitude();
		String lng = modelVefat.getTaziye_konum().getLongitude();
		
		if (lat.length() != 0 && lng.length() != 0) {
		  Double latitude = Double.parseDouble(lat);
		  Double longitude = Double.parseDouble(lng);
		  CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", modelVefat.getMezarlik(), "",
					 latitude, longitude);
		} else {
		  Toast.makeText(mContext, "Konum bilgisi girilmemiş", Toast.LENGTH_SHORT).show();
		}
		
		
	 });
	 
	 holder.button_mezarlik_adresi.setOnClickListener(v -> {
		
		String lat = modelVefat.getMezarlik_konum().getLatitude();
		String lng = modelVefat.getMezarlik_konum().getLongitude();
		
		if (lat.length() != 0 && lng.length() != 0) {
		  Double latitude = Double.parseDouble(lat);
		  Double longitude = Double.parseDouble(lng);
		  CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", modelVefat.getMezarlik(), "",
					 latitude, longitude);
		} else {
		  Toast.makeText(mContext, "Konum bilgisi girilmemiş", Toast.LENGTH_SHORT).show();
		}
		
	 });
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  public class VefatViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView _name, _ana_baba, _dogum_yeri_yil, _yer_vakit, _mezarlik, _taziye_adresi;
	 private Button button_taziye_adresi, button_mezarlik_adresi;
	 
	 public VefatViewHolder(@NonNull View itemView) {
		super(itemView);
		
		_name = itemView.findViewById(R.id.vefat_name);
		_ana_baba = itemView.findViewById(R.id.vefat_ana_baba);
		_dogum_yeri_yil = itemView.findViewById(R.id.vefat_dogum_yeri_yili);
		_yer_vakit = itemView.findViewById(R.id.vefat_yer_vakit);
		_mezarlik = itemView.findViewById(R.id.vefat_mezarlik);
		_taziye_adresi = itemView.findViewById(R.id.vefat_taziye_adres);
		button_taziye_adresi = itemView.findViewById(R.id.button_taziye_adresi);
		button_mezarlik_adresi = itemView.findViewById(R.id.button_mezarlik_adresi);
		
	 }
  }
}
