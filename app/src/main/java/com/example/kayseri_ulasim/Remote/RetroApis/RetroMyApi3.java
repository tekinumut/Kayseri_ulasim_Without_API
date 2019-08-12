package com.example.kayseri_ulasim.Remote.RetroApis;

import com.example.kayseri_ulasim.Model.ModelOthers.ModelBisiklet;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelOtopark;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetroMyApi3 {
  
  @GET("???.php")
  Call<List<ModelOtopark>> getOtoparkValues();
  
  @GET("???.php")
  Call<List<ModelBisiklet>> getBisikletValues();
  
}
