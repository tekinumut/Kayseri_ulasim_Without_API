package com.example.kayseri_ulasim.Remote.RetroApis;


import com.example.kayseri_ulasim.Model.ModelCheckInternet;
import com.example.kayseri_ulasim.Model.ModelMesafeOfHat;
import com.example.kayseri_ulasim.Model.ModelStationsResponse;
import com.example.kayseri_ulasim.Model.ModelToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroMyApi {
  
  @GET("???.php")
  Call<ModelCheckInternet> getInternetStatus();
  
  @GET("???.php.php")
  Call<ModelToken> getTokenValue();
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getNearbyStations(@Field("latitude") Double latitude, @Field("longitude") Double longitude,
																@Field("count") int count);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getSearchedStations(@Field("id") String id, @Field("name") String name,
																  @Field("latitude") Double latitude, @Field("longitude") Double longitude);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getStationsOnMap(@Field("latitude") Double latitude, @Field("longitude") Double longitude,
															  @Field("lat_center") Double lat_center, @Field("long_center") Double long_center,
															  @Field("lat_south") Double lat_south, @Field("lat_north") Double lat_north,
															  @Field("long_south") Double long_south, @Field("long_north") Double long_north);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getStationBarcode(@Field("id") int id, @Field("latitude") Double latitude, @Field("longitude") Double longitude);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getDistanceOfFav(@Field("id") String id, @Field("latitude") Double latitude, @Field("longitude") Double longitude);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getLinesOfStops(@Field("busstopid") String busstopid);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getBusLines(@Field("id") String id, @Field("name") String name);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getStopsOfLines(@Field("hatid") int hatid);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getHowToGo(@Field("begin_lat") Double begin_lat, @Field("begin_lng") Double begin_lng,
													  @Field("end_lat") Double end_lat, @Field("end_lng") Double end_lng,
													  @Field("count1") int count1, @Field("count2") int count2);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelMesafeOfHat> getMesafeOfHat(@Field("hatid") int hatid, @Field("beginStop") int beginStop,
													 @Field("endStop") int endStop);
  
  @FormUrlEncoded
  @POST("???.php.php")
  Call<ModelStationsResponse> getLBetweenS(@Field("hatid") int hatid, @Field("beginStop") int beginStop,
														 @Field("endStop") int endStop);
  
}
