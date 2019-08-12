package com.example.kayseri_ulasim.Model;

public class ModelHowToGo {
  
  private int hatid, hatkod, beginStop, endStop;
  private String hatad, beginName, endName;
  private double beginLat, beginLng, endLat, endLng;
  
  public ModelHowToGo(int hatid, int hatkod, int beginStop, int endStop, String hatad, String beginName, String endName,
							 double beginLat, double beginLng, double endLat, double endLng) {
	 this.hatid = hatid;
	 this.hatkod = hatkod;
	 this.beginStop = beginStop;
	 this.endStop = endStop;
	 this.hatad = hatad;
	 this.beginName = beginName;
	 this.endName = endName;
	 this.beginLat = beginLat;
	 this.beginLng = beginLng;
	 this.endLat = endLat;
	 this.endLng = endLng;
  }
  
  public int getHatid() {
	 return hatid;
  }
  
  public int getHatkod() {
	 return hatkod;
  }
  
  public int getBeginStop() {
	 return beginStop;
  }
  
  public int getEndStop() {
	 return endStop;
  }
  
  public String getHatad() {
	 return hatad;
  }
  
  public String getBeginName() {
	 return beginName;
  }
  
  public String getEndName() {
	 return endName;
  }
  
  public double getBeginLat() {
	 return beginLat;
  }
  
  public double getBeginLng() {
	 return beginLng;
  }
  
  public double getEndLat() {
	 return endLat;
  }
  
  public double getEndLng() {
	 return endLng;
  }
}
