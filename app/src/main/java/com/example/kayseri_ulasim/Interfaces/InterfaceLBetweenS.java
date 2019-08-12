package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelLinesBetweenStops;

import java.util.List;

public interface InterfaceLBetweenS {
  
  void onResponse(String situation, List<ModelLinesBetweenStops> linesBetweenStops);
}
