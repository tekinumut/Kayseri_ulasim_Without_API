package com.example.kayseri_ulasim.Remote;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.Model.ModelUpdateLocation;
import com.example.kayseri_ulasim.R;

@Database(entities = {ModelStationList.class, ModelUpdateLocation.class}, version = 4, exportSchema = false)
public abstract class FavoritesRoomDatabase extends RoomDatabase {
  
  private static FavoritesRoomDatabase instance;
  
  public abstract FavoritesDao myFavoritesDao();
  
  public static synchronized FavoritesRoomDatabase getInstance(Context mContext) {
	 if (instance == null) {
		instance = Room.databaseBuilder(mContext, FavoritesRoomDatabase.class, mContext.getString(R.string.favorites_database_name))
				  .fallbackToDestructiveMigration()
				  .allowMainThreadQueries()
				  .build();
	 }
	 return instance;
  }
}
