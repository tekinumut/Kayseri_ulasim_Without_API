package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelStopsOfLines;

import java.util.List;

public interface InterfaceStopsOfLines {
  
  void onResponse(String situation,List<ModelStopsOfLines> modelStopsOfLines);
}
