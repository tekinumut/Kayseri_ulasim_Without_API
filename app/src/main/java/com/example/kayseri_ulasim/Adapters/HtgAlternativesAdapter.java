package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Fragments.HtgMapFragment;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelHowToGoAll;
import com.example.kayseri_ulasim.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class HtgAlternativesAdapter extends RecyclerView.Adapter<HtgAlternativesAdapter.HtgViewHolder> {
  
  private Context mContext;
  private List<ModelHowToGoAll> recyclerList;
  private HashMap<String, ModelHowToGoAll> hashMap;
  
  public HtgAlternativesAdapter(Context mContext, List<ModelHowToGoAll> recyclerList, HashMap<String, ModelHowToGoAll> hashMap) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
	 this.hashMap = hashMap;
  }
  
  @NonNull
  @Override
  public HtgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 View mView = LayoutInflater.from(mContext).inflate(R.layout.rec_inside_htg_alternatives, parent, false);
	 
	 return new HtgAlternativesAdapter.HtgViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull HtgViewHolder holder, int position) {
	 ModelHowToGoAll model = recyclerList.get(position);
	 
	 holder.ilkDuragaUzaklik.setText(CallMethods.yourDistanceAsText(model.getIlkDuragaUzaklik()));
	 
	 holder.sonDuragaUzaklik.setText(CallMethods.yourDistanceAsText(model.getSonDuragaUzaklik()));
	 
	 holder.destination_konumum.setText(CallMethods.yourDistanceAsText(model.getKonumdanUzaklik()));
	 
	 RestMethods.getMesafeOfHat(model.getHtgList().getHatid(), model.getHtgList().getBeginStop(), model.getHtgList().getEndStop(), mesafe -> {
		//
		if (mesafe == 0) {
		  holder.toplamHatMesafesi.setText("Toplam hat uzunluğu bilinmiyor");
		} else
		  holder.toplamHatMesafesi.setText("Toplam hat uzunluğu: " + CallMethods.yourDistanceAsText((double) mesafe));
	 });
	 
	 holder.toplamYurumeMesafesi.setText("Toplam yürüme mesafesi: " + CallMethods.yourDistanceAsText(model.getToplamYurumeMesafesi()));
	 
	 
	 holder.itemView.setOnClickListener(v -> {
		HtgMapFragment htgFragment = new HtgMapFragment();
		Bundle bundle = new Bundle();
		
		bundle.putString("jsonHowToGoAll", new Gson().toJson(model));
		bundle.putSerializable("hashmap", hashMap);
		
		htgFragment.setArguments(bundle);
		FragmentTransaction fragmentTransaction = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		fragmentTransaction.add(R.id.fragment_container, htgFragment).addToBackStack("howtogoadapter")
				  .commit();
	 });
  }
  
  @Override
  public int getItemCount() {
	 if (recyclerList.size() > 10)
		return 10;
	 else
		return recyclerList.size();
  }
  
  public class HtgViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView ilkDuragaUzaklik, sonDuragaUzaklik, destination_konumum, toplamYurumeMesafesi, toplamHatMesafesi;
	 
	 public HtgViewHolder(@NonNull View itemView) {
		super(itemView);
		ilkDuragaUzaklik = itemView.findViewById(R.id.destination_start);
		sonDuragaUzaklik = itemView.findViewById(R.id.destination_end);
		destination_konumum = itemView.findViewById(R.id.destination_konumum);
		toplamYurumeMesafesi = itemView.findViewById(R.id.destination_total_yurume);
		toplamHatMesafesi = itemView.findViewById(R.id.destination_total_hat);
		
	 }
  }
  
}
