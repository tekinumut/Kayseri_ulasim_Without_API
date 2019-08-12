package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

public class ModelMezarlikKonum {

  @SerializedName("lat")
  private String latitude;
  @SerializedName("lng")
  private String longitude;
  
  public ModelMezarlikKonum(String latitude, String longitude) {
	 this.latitude = latitude;
	 this.longitude = longitude;
  }
  
  public String getLatitude() {
	 return latitude;
  }
  
  public String getLongitude() {
	 return longitude;
  }
}
