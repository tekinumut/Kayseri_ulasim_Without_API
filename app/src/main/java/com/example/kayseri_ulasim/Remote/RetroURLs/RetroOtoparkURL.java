package com.example.kayseri_ulasim.Remote.RetroURLs;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Remote.RetroApis.RetroOtoparkApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroOtoparkURL {
  
  private static Retrofit retrofit = null;
  private static final String BASE_URL = "https://www.google.com/?????";
  
  
  public static RetroOtoparkApi getmyApi() {
	 if (retrofit == null) {
		
		retrofit = new Retrofit.Builder()
				  .baseUrl(BASE_URL)
				  .addConverterFactory(GsonConverterFactory.create())
				  .client(CallMethods.setTimeout(10))
				  .build();
	 }
	 return retrofit.create(RetroOtoparkApi.class);
  }
}
