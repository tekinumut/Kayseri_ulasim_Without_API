package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

public class ModelOtopark {
  
  @SerializedName("Otopark")
  private String name;
  @SerializedName("Katli")
  private boolean katli;
  @SerializedName("Lat")
  private String latitude;
  @SerializedName("Lng")
  private String longitude;
  private Double distance;
  
  public ModelOtopark(String name, boolean katli, String latitude, String longitude, Double distance) {
	 this.name = name;
	 this.katli = katli;
	 this.latitude = latitude;
	 this.longitude = longitude;
	 this.distance = distance;
  }
  
  public String getName() {
	 return name;
  }
  
  public boolean isKatli() {
	 return katli;
  }
  
  public String getLatitude() {
	 return latitude;
  }
  
  public String getLongitude() {
	 return longitude;
  }
  
  public Double getDistance() {
	 return distance;
  }
}
