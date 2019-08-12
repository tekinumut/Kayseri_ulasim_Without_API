package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelEczaneResponse {
  
  @SerializedName("Data")
  private List<ModelEczane> modelEczanes;
  
  public ModelEczaneResponse(List<ModelEczane> modelEczanes) {
	 this.modelEczanes = modelEczanes;
  }
  
  public List<ModelEczane> getModelEczanes() {
	 return modelEczanes;
  }
}
