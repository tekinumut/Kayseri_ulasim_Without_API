package com.example.kayseri_ulasim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Model.ModelHtgMap;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class HtgMapAdapter extends RecyclerView.Adapter<HtgMapAdapter.HtgMapViewHolder> {
  
  private Context mContext;
  private List<ModelHtgMap> recyclerList;
  
  public HtgMapAdapter(Context mContext, List<ModelHtgMap> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public HtgMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	 View mView = View.inflate(mContext, R.layout.rec_inside_htg_map, null);
	 
	 return new HtgMapAdapter.HtgMapViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull HtgMapViewHolder holder, int position) {
	 ModelHtgMap modelHTG = recyclerList.get(position);
	 
	 holder.title.setText(modelHTG.getTitle());
	 holder.snippet.setText(modelHTG.getSnippet());
	 holder.imageView.setImageBitmap(modelHTG.getIcon());
	 
  }
  
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  public class HtgMapViewHolder extends RecyclerView.ViewHolder {
	 
	 private TextView title, snippet;
	 private ImageView imageView;
	 
	 public HtgMapViewHolder(@NonNull View itemView) {
		super(itemView);
		
		title = itemView.findViewById(R.id.title_htgmap);
		snippet = itemView.findViewById(R.id.snippet_htgmap);
		imageView = itemView.findViewById(R.id.imageView_htgmap);
	 }
  }
  
  
}
