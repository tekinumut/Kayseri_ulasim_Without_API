package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Fragments.StopsOfLineFragment;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelLineofStops;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class LinesOfStopsAdapter extends RecyclerView.Adapter<LinesOfStopsAdapter.BusLinesOfStopsViewHolder> {
  
  private Context mContext;
  private List<ModelLineofStops> recyclerList;
  private Dialog dialog;
  
  public LinesOfStopsAdapter(Context mContext, List<ModelLineofStops> recyclerList, Dialog dialog) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
	 this.dialog = dialog;
  }
  
  
  @NonNull
  @Override
  public BusLinesOfStopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 @SuppressLint("InflateParams")
	 View mView = LayoutInflater.from(mContext).inflate(R.layout.rec_inside_tab3, null, false);
	 return new LinesOfStopsAdapter.BusLinesOfStopsViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull BusLinesOfStopsViewHolder holder, int position) {
	 ModelLineofStops model = recyclerList.get(position);
	 
	 holder.gecen_hatlar.setText(model.getKod() + " - " + model.getAd());
	 
	 holder.itemView.setOnClickListener(v -> {
		
		StopsOfLineFragment stops = new StopsOfLineFragment();
		Bundle bundle = new Bundle();
		
		bundle.putInt(mContext.getString(R.string.bundle_bus_line), model.getId());
		bundle.putInt(mContext.getString(R.string.bundle_bus_line_kod), model.getKod());
		bundle.putString(mContext.getString(R.string.bundle_bus_line_name), model.getAd());
		
		//Tab1'de geçen hatlar dialog penceresi kapanmadığı için kullandık
		if (dialog != null)
		  dialog.dismiss();
		
		stops.setArguments(bundle);
		((MainActivity) mContext).getSupportFragmentManager().beginTransaction()
				  .add(R.id.fragment_container, stops).addToBackStack("adapter").commit();
	 });
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  public class BusLinesOfStopsViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView gecen_hatlar;
	 
	 public BusLinesOfStopsViewHolder(@NonNull View itemView) {
		super(itemView);
		gecen_hatlar = itemView.findViewById(R.id.gecen_hatlar);
		
	 }
  }
  
}
