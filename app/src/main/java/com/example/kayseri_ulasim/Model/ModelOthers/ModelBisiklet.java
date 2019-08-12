package com.example.kayseri_ulasim.Model.ModelOthers;

public class ModelBisiklet {
  
  private String DurakAdi;
  private int DoluSlot;
  private int BosSlot;
  private String Lat;
  private String Lng;
  private double distance;
  
  public ModelBisiklet(String durakAdi, int doluSlot, int bosSlot, String lat, String lng, double distance) {
	 DurakAdi = durakAdi;
	 DoluSlot = doluSlot;
	 BosSlot = bosSlot;
	 Lat = lat;
	 Lng = lng;
	 this.distance = distance;
  }
  
  public String getDurakAdi() {
	 return DurakAdi;
  }
  
  public int getDoluSlot() {
	 return DoluSlot;
  }
  
  public int getBosSlot() {
	 return BosSlot;
  }
  
  public String getLat() {
	 return Lat;
  }
  
  public String getLng() {
	 return Lng;
  }
  
  public double getDistance() {
	 return distance;
  }
}
