package com.example.kayseri_ulasim.Remote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.Model.ModelUpdateLocation;

import java.util.List;

@Dao
public interface FavoritesDao {
  
  @Insert
  void addFavoriteItem(ModelStationList modelFavorites);
  
  @Query("Delete from Favorites where id =:id")
  void removeFavoriteItem(int id);
  
  @Query("Select *from Favorites Order By distance DESC LIMIT 50")
  List<ModelStationList> getRecords();
  
  //Eğer gönderilen id var ise 1 yok ise 0 dönderir
  @Query("SELECT CASE WHEN EXISTS (SELECT id FROM Favorites WHERE id= :id) " +
			 "THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
  int isItFav(int id);
  
  
  @Query("Update Favorites set distance =:distance where id =:id")
  void updateDistance(Double distance, int id);
  
  // || java'da + demek
  @Query("Select * from Favorites where id like '%' || :id || '%' or name like '%' || :name || '%'")
  List<ModelStationList> getSearchedRecords(String id, String name);
  
  @Query("Delete from UpdateLocation")
  void deleteUpdateLocation();
  
  @Insert
  void addNewLocation(ModelUpdateLocation modelUpdateLocation);
  
  @Query("Select *from UpdateLocation")
  LiveData<List<ModelUpdateLocation>> getLocation();
 
}
