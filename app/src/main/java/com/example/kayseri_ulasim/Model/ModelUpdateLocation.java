package com.example.kayseri_ulasim.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UpdateLocation")
public class ModelUpdateLocation {
  
  @PrimaryKey()
  public int id;
  private Double latitude;
  private Double longitude;
  
  public ModelUpdateLocation(Double latitude, Double longitude) {
	 this.latitude = latitude;
	 this.longitude = longitude;
  }
  
  public int getId() {
	 return id;
  }
  
  public Double getLatitude() {
	 return latitude;
  }
  
  public Double getLongitude() {
	 return longitude;
  }
}
