package com.example.kayseri_ulasim.Model;

import com.google.gson.annotations.SerializedName;

public class ModelLineofStops {
//  "Hat_id": 137,
	//		 "Hat_Kod": 550,
	//		 "Hat_Ad": "TALAS ANAYURT JANDARMA (TALAS - > YOĞUNBURÇ)"
  
  @SerializedName("hatid")
  private int id;
  @SerializedName("hatkod")
  private int kod;
  @SerializedName("hatad")
  private String ad;
  
  public ModelLineofStops(int id, int kod, String ad) {
	 this.id = id;
	 this.kod = kod;
	 this.ad = ad;
  }
  
  public int getId() {
	 return id;
  }
  
  public int getKod() {
	 return kod;
  }
  
  public String getAd() {
	 return ad;
  }
}
