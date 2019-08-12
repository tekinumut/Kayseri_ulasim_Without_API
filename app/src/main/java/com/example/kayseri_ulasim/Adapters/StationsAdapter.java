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
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsViewHolder> {
  
  private Context mContext;
  private final List<ModelStationList> recyclerList;
  
  public StationsAdapter(Context mContext, List<ModelStationList> recyclerList) {
	 this.mContext = mContext;
	 this.recyclerList = recyclerList;
  }
  
  @NonNull
  @Override
  public StationsAdapter.StationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
	 
	 View mView = View.inflate(mContext, R.layout.rec_inside_stations, null);
	 return new StationsViewHolder(mView);
  }
  
  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NonNull final StationsAdapter.StationsViewHolder holder, int position) {
	 
	 final ModelStationList model = recyclerList.get(position);
	 
	 holder.station_name.setText("(Durak No: " + model.getId() + ") - " + model.getName());
	 
	 holder.station_distance.setText("Uzaklığınız: " + CallMethods.yourDistanceAsText(model.getDistance()));
	 
	 holder.yol_tarifi.setOnClickListener(v ->
				CallMethods.getChooseDirectionsDialog(mContext, "StationsAdapter", model.getName(), "Durak No: " + model.getId(), model.getLatitude(), model.getLongitude()));
	 
	 //Eğer durak favorilerde ise o durağın like butonunu true yap.
	 if (FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().isItFav(model.getId()) == 1) {
		holder.add_favorite.setLiked(true);
	 } else
		holder.add_favorite.setLiked(false);
	 
	 //Hangi durak için hatları istiyorsam o durağın id'sini gönder. O Durak id'sine göre
	 //Geçen Hatlar'da o duraktan geçen hatlar listelenecek
	 holder.gecen_otobusler.setOnClickListener(v -> {
		//Durak id gönder
		//0 = gececekotobusler
		CallMethods.getBusLineDialog(mContext, model.getId(),0);
	 });
	 
	 holder.gecen_hatlar.setOnClickListener(v -> {
		
		//Durak id gönder
		//1 = gecenhatlar
		CallMethods.getBusLineDialog(mContext,model.getId(),1);
		
	 });
	 
	 holder.add_favorite.setOnLikeListener(new OnLikeListener() {
		@Override
		public void liked(LikeButton likeButton) {
		  
		  ModelStationList modelFavorites = new ModelStationList(model.getId(), CallMethods.toEnglish(model.getName()),
					 model.getLatitude(), model.getLongitude(), model.getDistance());
		  FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().addFavoriteItem(modelFavorites);
		  Toast.makeText(mContext, model.getName() + " favorilere eklendi", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void unLiked(LikeButton likeButton) {
		  FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().removeFavoriteItem(model.getId());
		  Toast.makeText(mContext, model.getName() + " favorilerden silindi", Toast.LENGTH_SHORT).show();
		  
		  if (CallMethods.imInFavorites(mContext)) {
			 recyclerList.remove(holder.getAdapterPosition());
			 notifyItemRemoved(holder.getAdapterPosition());
			 notifyItemRangeChanged(holder.getAdapterPosition(), recyclerList.size());
		  }
		  
		}
	 });
  }
  
  @Override
  public int getItemCount() {
	 return recyclerList.size();
  }
  
  class StationsViewHolder extends RecyclerView.ViewHolder {
	 
	 final TextView station_name, station_distance;
	 final Button yol_tarifi, gecen_otobusler, gecen_hatlar;
	 final LikeButton add_favorite;
	 
	 StationsViewHolder(View itemView) {
		super(itemView);
		
		station_name = itemView.findViewById(R.id.station_name);
		station_distance = itemView.findViewById(R.id.station_distance);
		yol_tarifi = itemView.findViewById(R.id.yol_tarifi);
		gecen_otobusler = itemView.findViewById(R.id.gecen_otobusler);
		gecen_hatlar = itemView.findViewById(R.id.gecen_hatlar);
		add_favorite = itemView.findViewById(R.id.station_favorite_inside);
	 }
  }
  
}
