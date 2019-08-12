package com.example.kayseri_ulasim.AlertDialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;

//Tab2Stations haritada ki marker'a tıklanınca çıkan dialog
public class AlertMarkerInfoPanel extends DialogFragment {
  
  private Context mContext;
  private String name;
  private Double latitude;
  private Double longitude;
  private int bus_no;
  
  @SuppressLint("SetTextI18n")
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
	 
	 View view = View.inflate(mContext, R.layout.alert_marker_info_panel, null);
	 
	 Bundle bundle = getArguments();
	 if (bundle != null) {
		
		name = bundle.getString(getString(R.string.bundle_name));
		latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		longitude = bundle.getDouble(getString(R.string.bundle_longitude));
		bus_no = bundle.getInt(getString(R.string.bundle_bus_no));
	 }
	 
	 TextView bus_of_stops = view.findViewById(R.id.bus_of_stops);
	 TextView bus_of_lines = view.findViewById(R.id.bus_of_lines);
	 LikeButton fav_button = view.findViewById(R.id.station_favorite);
	 TextView fav_status = view.findViewById(R.id.station_fav_status);
	 TextView get_direction = view.findViewById(R.id.get_direction);
	 
	 if (FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().isItFav(bus_no) == 1) {
		fav_status.setText("Favorilerimden Kaldır");
		fav_button.setLiked(true);
	 } else {
		fav_status.setText("Favorilerime Ekle");
		fav_button.setLiked(false);
	 }
	 
	 
	 bus_of_stops.setOnClickListener(v -> {
		//Durak id gönder
		CallMethods.getBusLineDialog(mContext, bus_no, 0);
		if (getDialog() != null)
		  getDialog().dismiss();
		
	 });
	 
	 bus_of_lines.setOnClickListener(v -> {
		//Durak id gönder
		CallMethods.getBusLineDialog(mContext, bus_no, 1);
		if (getDialog() != null)
		  getDialog().dismiss();
	 });
	 
	 fav_button.setOnLikeListener(new OnLikeListener() {
		@Override
		public void liked(LikeButton likeButton) {
		  ModelStationList model = new ModelStationList(bus_no, CallMethods.toEnglish(name), latitude, longitude, 0.0);
		  FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao()
					 .addFavoriteItem(model);
		  fav_status.setText("Favorilerimden Kaldır");
		  Toast.makeText(mContext, name + " favorilere eklendi", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void unLiked(LikeButton likeButton) {
		  FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().removeFavoriteItem(bus_no);
		  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
		  fav_status.setText("Favorilerime Ekle");
		  Toast.makeText(mContext, name + " favorilerden silindi", Toast.LENGTH_SHORT).show();
		}
	 });
	 
	 get_direction.setOnClickListener(v -> {
		//Yol tarifi al
		CallMethods.getChooseDirectionsDialog(mContext, "AlertMarkerInfoPanel", name, "Durak No: " + bus_no, latitude, longitude);
		if (getDialog() != null)
		  getDialog().dismiss();
	 });


     /*   DialogInterface.OnClickListener listener = (dialog, which) -> {
            Toast.makeText(mContext, "Boş bir alana tıklayarakta kapatabilirsiniz.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        };*/
	 
	 return new AlertDialog.Builder(mContext)
				.setView(view)
				.create();
  }
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
}
