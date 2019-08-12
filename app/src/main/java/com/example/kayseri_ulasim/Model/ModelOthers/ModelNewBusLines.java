package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelNewBusLines {


  @SerializedName("OzelBilgi")
  private List<ModelBusLinesOzelBilgi> ozelBilgis;
  @SerializedName("HatId")
  private int hatId;
  @SerializedName("HatAdi")
  private String hatAdi;
  @SerializedName("HatKod")
  private int hatKod;
  @SerializedName("HatUrl")
  private String hatUrl;
  
  public ModelNewBusLines(List<ModelBusLinesOzelBilgi> ozelBilgis, int hatId, String hatAdi, int hatKod, String hatUrl) {
	 this.ozelBilgis = ozelBilgis;
	 this.hatId = hatId;
	 this.hatAdi = hatAdi;
	 this.hatKod = hatKod;
	 this.hatUrl = hatUrl;
  }
  
  public List<ModelBusLinesOzelBilgi> getOzelBilgis() {
	 return ozelBilgis;
  }
  
  public int getHatId() {
	 return hatId;
  }
  
  public String getHatAdi() {
	 return hatAdi;
  }
  
  public int getHatKod() {
	 return hatKod;
  }
  
  public String getHatUrl() {
	 return hatUrl;
  }
}
