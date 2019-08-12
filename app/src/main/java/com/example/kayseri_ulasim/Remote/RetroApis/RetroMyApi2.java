package com.example.kayseri_ulasim.Remote.RetroApis;

import com.example.kayseri_ulasim.Model.ModelOthers.ModelBusLinesResponse;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBusLocationResponse;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelEczaneResponse;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelVefatResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroMyApi2 {
  
  @GET("???.php/???.php")
  Call<ModelEczaneResponse> getEczaneValues(@Query("token") String token);
  
  @GET("???.php/???.php")
  Call<ModelBusLinesResponse> getTimesOfLines(@Query("token") String token, @Query("DurakId") int DurakId);
  
  @GET("???.php/???.php")
  Call<ModelVefatResponse> getVefatValues(@Query("token") String token, @Query("Tarih") String Tarih);
  
  @GET("???.php/???.php")
  Call<ModelBusLocationResponse> getOtobusKonum(@Query("hatid") int hatid, @Query("token") String token);
  
}
