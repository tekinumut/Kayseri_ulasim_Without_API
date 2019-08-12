package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelBusLocationBilgi {
  
  @SerializedName("OtobusBilgisi")
  List<ModelBusLocation> otobusKonum;
  
  public ModelBusLocationBilgi(List<ModelBusLocation> otobusKonum) {
	 this.otobusKonum = otobusKonum;
  }
  
  public List<ModelBusLocation> getOtobusKonum() {
	 return otobusKonum;
  }
}
