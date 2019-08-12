package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelLineofStops;

import java.util.List;

public interface InterfaceBusLines {
  
  void onResponse(String situation, List<ModelLineofStops> modelLineofStops);
  
}
