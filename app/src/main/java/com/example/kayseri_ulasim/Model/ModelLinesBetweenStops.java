package com.example.kayseri_ulasim.Model;

public class ModelLinesBetweenStops {
  
  private Double lat,lng;
  
  public ModelLinesBetweenStops(Double lat, Double lng) {
	 this.lat = lat;
	 this.lng = lng;
  }
  
  public Double getLat() {
	 return lat;
  }
  
  public Double getLng() {
	 return lng;
  }
}
