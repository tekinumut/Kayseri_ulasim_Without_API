package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

public class ModelEczane {
  
  @SerializedName("EczaneAdi")
  private String name;
  @SerializedName("NobetTuru")
  private String nobet_turu;
  @SerializedName("Adres")
  private String adres;
  @SerializedName("Tel")
  private String phone_number;
  @SerializedName("Ilce")
  private String ilce;
  @SerializedName("Latitude")
  private String latitude;
  @SerializedName("Longitude")
  private String longitude;
  private Double distance;
  
  public ModelEczane(String name, String nobet_turu, String adres, String phone_number, String ilce, String latitude, String longitude,Double distance) {
	 this.name = name;
	 this.nobet_turu = nobet_turu;
	 this.adres = adres;
	 this.phone_number = phone_number;
	 this.ilce = ilce;
	 this.latitude = latitude;
	 this.longitude = longitude;
	 this.distance = distance;
  }
  
  public String getName() {
	 return name;
  }
  
  public String getNobet_turu() {
	 return nobet_turu;
  }
  
  public String getAdres() {
	 return adres;
  }
  
  public String getPhone_number() {
	 return phone_number;
  }
  
  public String getIlce() {
	 return ilce;
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
