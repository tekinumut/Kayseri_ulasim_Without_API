package com.example.kayseri_ulasim.Remote.RetroURLs;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Remote.RetroApis.RetroMyApi2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroBaseURL2 {
  
  private static Retrofit retrofit = null;
  private static final String BASE_URL = "http://google????/???Aa/";
  
  
  public static RetroMyApi2 getmyApi() {
	 if (retrofit == null) {
		
		retrofit = new Retrofit.Builder()
				  .baseUrl(BASE_URL)
				  .addConverterFactory(GsonConverterFactory.create())
				  .client(CallMethods.setTimeout(10))
				  .build();
	 }
	 return retrofit.create(RetroMyApi2.class);
  }
  
}
