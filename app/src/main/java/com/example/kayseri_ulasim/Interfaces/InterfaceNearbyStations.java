package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelStationList;

import java.util.List;

public interface InterfaceNearbyStations {
  
  void onResponse(String situation, List<ModelStationList> modelStationList);
}
