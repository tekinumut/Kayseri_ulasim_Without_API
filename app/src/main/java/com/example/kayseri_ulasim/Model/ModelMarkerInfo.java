package com.example.kayseri_ulasim.Model;

public class ModelMarkerInfo {
  
  
  private int busid;
  private String name;
  private Double lat;
  private Double lng;
  private Double distance;
  private int mesafe;
  private String items;
  
  public ModelMarkerInfo(int busid, String name, Double lat, Double lng, Double distance, int mesafe, String items) {
	 this.busid = busid;
	 this.name = name;
	 this.lat = lat;
	 this.lng = lng;
	 this.distance = distance;
	 this.mesafe = mesafe;
	 this.items = items;
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
  
  public Double getDistance() {
	 return distance;
  }
  
  public int getMesafe() {
	 return mesafe;
  }
  
  public String getItems() {
	 return items;
  }
}
