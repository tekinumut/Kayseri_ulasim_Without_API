package com.example.kayseri_ulasim.Model.ModelOthers;


import com.google.gson.annotations.SerializedName;

public class ModelBusLocationResponse {
  
  @SerializedName("Data")
  private ModelBusLocationBilgi otobusKonumResponse;
  
  public ModelBusLocationResponse(ModelBusLocationBilgi otobusKonumResponse) {
	 this.otobusKonumResponse = otobusKonumResponse;
  }
  
  public ModelBusLocationBilgi getOtobusKonumResponse() {
	 return otobusKonumResponse;
  }
}
