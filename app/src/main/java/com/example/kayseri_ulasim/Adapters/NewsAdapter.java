package com.example.kayseri_ulasim.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kayseri_ulasim.AlertDialogs.AlertNews;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelNews;
import com.example.kayseri_ulasim.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
  
  private Context mContext;
  private final List<ModelNews> recyclerList;
  
  public NewsAdapter(Context mContext, List<ModelNews> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
	 
	 View mView = View.inflate(mContext, R.layout.rec_inside_news, null);
	 return new NewsViewHolder(mView, mContext);
  }
  
  @Override
  public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
	 
	 ModelNews modelNews = recyclerList.get(position);
	 holder.title.setText(modelNews.getTitle());
	 holder.date.setText(modelNews.getTime());

    /*   if (modelNews.getDetail().length() > 99) {
            String cut_text = modelNews.getDetail().substring(0, 96);
            StringBuilder builder = new StringBuilder();

           // holder.title_detail.setText(builder.append(cut_text).append("..."));
        } else {
            holder.title_detail.setText(modelNews.getDetail());
        }
    */
	 Glide.with(mContext)
				.load(modelNews.getImage_url())
				.into(holder.image_news);
	 
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  class NewsViewHolder extends RecyclerView.ViewHolder {
	 
	 ImageView image_news;
	 TextView title, date;
	 
	 NewsViewHolder(View itemView, Context context) {
		super(itemView);
		mContext = context;
		image_news = itemView.findViewById(R.id.image_news);
		title = itemView.findViewById(R.id.title_news);
		//  title_detail = itemView.findViewById(R.id.title_detail_news);
		date = itemView.findViewById(R.id.date_news);
		
		
		itemView.setOnClickListener(v -> {
		  
		  //Ana sayfadaki verileri, haber detayını görüntüleyeceğimiz fragment'a gönderiyoruz.
		  ModelNews modelNews = recyclerList.get(getAdapterPosition());
		  
		  FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
		  Bundle bundle = new Bundle();
		  AlertNews newsPage = new AlertNews();
		  
		  bundle.putString(mContext.getString(R.string.bundle_news_title), modelNews.getTitle());
		  bundle.putString(mContext.getString(R.string.bundle_news_image_url), modelNews.getImage_url());
		  bundle.putString(mContext.getString(R.string.bundle_news_content), modelNews.getContent());
		  bundle.putString(mContext.getString(R.string.bundle_news_page_content_url), modelNews.getPage_content_url());
		  
		  newsPage.setArguments(bundle);
		  newsPage.show(manager, null);
		});
		
		
	 }
  }
  
  
}
