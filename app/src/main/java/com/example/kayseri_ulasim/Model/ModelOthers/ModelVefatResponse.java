package com.example.kayseri_ulasim.Model.ModelOthers;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelVefatResponse {
  
  @SerializedName("Data")
  private List<ModelVefat> modelVefats;
  
  public ModelVefatResponse(List<ModelVefat> modelVefats) {
	 this.modelVefats = modelVefats;
  }
  
  public List<ModelVefat> getModelVefats() {
	 return modelVefats;
  }
}
