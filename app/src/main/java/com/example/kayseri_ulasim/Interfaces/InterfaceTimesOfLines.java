package com.example.kayseri_ulasim.Interfaces;

import com.example.kayseri_ulasim.Model.ModelOthers.ModelNewBusLines;

import java.util.List;

public interface InterfaceTimesOfLines {
  void onResponse(String situation, List<ModelNewBusLines> model);
}
