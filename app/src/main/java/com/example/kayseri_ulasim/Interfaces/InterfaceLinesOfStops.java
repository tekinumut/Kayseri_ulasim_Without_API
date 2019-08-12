package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelLineofStops;

import java.util.List;

public interface InterfaceLinesOfStops {
  
  void onResponse(String situation, List<ModelLineofStops> modelLinesOfStops);
}
