package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Model.ModelOthers.ModelNewBusLines;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class BusLinesAdapter extends RecyclerView.Adapter<BusLinesAdapter.BusLinesViewHolder> {
  
  private Context mContext;
  private List<ModelNewBusLines> recyclerList;
  
  public BusLinesAdapter(Context mContext, List<ModelNewBusLines> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public BusLinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 @SuppressLint("InflateParams")
	 View mView = LayoutInflater.from(mContext).inflate(R.layout.rec_inside_bus_lines, null, false);
	 mView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	 return new BusLinesAdapter.BusLinesViewHolder(mView, mContext);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull BusLinesViewHolder holder, int position) {
	 
	 ModelNewBusLines model = recyclerList.get(position);
	 
	 holder.line_name.setText(model.getHatAdi());
	 SpannableStringBuilder builder1 = new SpannableStringBuilder();
	 
	 for (int i = 0; i < model.getOzelBilgis().size(); i++) {
		
		int size = builder1.length();
		if (model.getOzelBilgis().get(i).getAciklama().contains("yaklaşık")) {
		  builder1.append("\u2022 ").append(model.getOzelBilgis().get(i).getAciklama())
					 .setSpan(new ForegroundColorSpan(Color.RED), size + 30,
								size + 30 + model.getOzelBilgis().get(i).getDakika().length(), 0);
		  builder1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),size + 30,
					 size + 30 + model.getOzelBilgis().get(i).getDakika().length(), 0);
		  if (i != model.getOzelBilgis().size() - 1)
		  builder1.append("\n");
		  
		} else {
		  builder1.append("\u2022 ").append(model.getOzelBilgis().get(i).getAciklama());
		  if (i != model.getOzelBilgis().size() - 1)
			 builder1.append("\n");
		}
		
		if (i == model.getOzelBilgis().size() - 1) {
		  holder.kalan_zaman.setText(builder1);
		}
	 }
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  class BusLinesViewHolder extends RecyclerView.ViewHolder {
	 
	 final TextView line_name, kalan_zaman;
	 
	 public BusLinesViewHolder(@NonNull View itemView, Context context) {
		super(itemView);
		mContext = context;
		line_name = itemView.findViewById(R.id.line_name);
		kalan_zaman = itemView.findViewById(R.id.rec__kalan_zaman);
		
	 }
  }
}
