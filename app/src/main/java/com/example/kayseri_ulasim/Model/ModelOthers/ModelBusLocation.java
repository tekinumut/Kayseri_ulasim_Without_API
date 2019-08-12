package com.example.kayseri_ulasim.Model.ModelOthers;

public class ModelBusLocation {

  
  private Double lat;
  private Double lng;
  private String Bilgi;
  
  public ModelBusLocation(Double lat, Double lng, String bilgi) {
	 this.lat = lat;
	 this.lng = lng;
	 Bilgi = bilgi;
  }
  
  public Double getLat() {
	 return lat;
  }
  
  public Double getLng() {
	 return lng;
  }
  
  public String getBilgi() {
	 return Bilgi;
  }
}
