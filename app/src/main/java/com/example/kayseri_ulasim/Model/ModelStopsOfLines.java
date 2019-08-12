package com.example.kayseri_ulasim.Model;


import com.google.gson.annotations.SerializedName;

public class ModelStopsOfLines {
  
  @SerializedName("id")
  private int busid;
  @SerializedName("name")
  private String name;
  @SerializedName("latitude")
  private Double lat;
  @SerializedName("longitude")
  private Double lng;
  @SerializedName("mesafe")
  private int oncekiDurakIleMesafe;
  
  public ModelStopsOfLines(int busid, String name, Double lat, Double lng, int oncekiDurakIleMesafe) {
	 this.busid = busid;
	 this.name = name;
	 this.lat = lat;
	 this.lng = lng;
	 this.oncekiDurakIleMesafe = oncekiDurakIleMesafe;
  }
  
  public int getBusid() {
	 return busid;
  }
  
  public String getName() {
	 return name;
  }
  
  public Double getLat() {
	 return lat;
  }
  
  public Double getLng() {
	 return lng;
  }
  
  public int getOncekiDurakIleMesafe() {
	 return oncekiDurakIleMesafe;
  }
}
