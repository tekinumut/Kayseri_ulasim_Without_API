package com.example.kayseri_ulasim.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelStationsResponse {
  
  private String error_msg;
  
  @SerializedName("stations")
  private List<ModelStationList> stations;
  @SerializedName("howtogo")
  private List<ModelHowToGo> howtogo;
  @SerializedName("linesOfStops")
  private List<ModelLineofStops> lineofStops;
  private List<ModelStopsOfLines> stopsOfLines;
  @SerializedName("lbetweens")
  private List<ModelLinesBetweenStops> linesBetweenStops;
  
  public ModelStationsResponse(String error_msg, List<ModelStationList> stations, List<ModelHowToGo> howtogo, List<ModelLineofStops> lineofStops,
										 List<ModelStopsOfLines> stopsOfLines, List<ModelLinesBetweenStops> linesBetweenStops) {
	 this.error_msg = error_msg;
	 this.stations = stations;
	 this.howtogo = howtogo;
	 this.lineofStops = lineofStops;
	 this.stopsOfLines = stopsOfLines;
	 this.linesBetweenStops = linesBetweenStops;
  }
  
  public String getError_msg() {
	 return error_msg;
  }
  
  public List<ModelStationList> getStations() {
	 return stations;
  }
  
  public List<ModelHowToGo> getHowtogo() {
	 return howtogo;
  }
  
  public List<ModelLineofStops> getLineofStops() {
	 return lineofStops;
  }
  
  public List<ModelStopsOfLines> getStopsOfLines() {
	 return stopsOfLines;
  }
  
  public List<ModelLinesBetweenStops> getLinesBetweenStops() {
	 return linesBetweenStops;
  }
}
