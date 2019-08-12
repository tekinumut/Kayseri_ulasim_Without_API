package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelBusLinesResponse {


  @SerializedName("Data")
  private List<ModelNewBusLines> busLines;
  
  public ModelBusLinesResponse(List<ModelNewBusLines> busLines) {
	 this.busLines = busLines;
  }
  
  public List<ModelNewBusLines> getTimesOfLines() {
	 return busLines;
  }
}
