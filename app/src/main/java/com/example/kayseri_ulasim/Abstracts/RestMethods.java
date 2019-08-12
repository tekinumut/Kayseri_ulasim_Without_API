package com.example.kayseri_ulasim.Abstracts;

import android.content.Context;

import com.example.kayseri_ulasim.Interfaces.InterFaceMesafeOfHat;
import com.example.kayseri_ulasim.Interfaces.InterfaceBusLines;
import com.example.kayseri_ulasim.Interfaces.InterfaceHowToGo;
import com.example.kayseri_ulasim.Interfaces.InterfaceLBetweenS;
import com.example.kayseri_ulasim.Interfaces.InterfaceLinesOfStops;
import com.example.kayseri_ulasim.Interfaces.InterfaceNearbyStations;
import com.example.kayseri_ulasim.Interfaces.InterfaceServerStatus;
import com.example.kayseri_ulasim.Interfaces.InterfaceStopsOfLines;
import com.example.kayseri_ulasim.Interfaces.InterfaceTimesOfLines;
import com.example.kayseri_ulasim.Interfaces.InterfaceTokenValue;
import com.example.kayseri_ulasim.Model.ModelCheckInternet;
import com.example.kayseri_ulasim.Model.ModelHowToGo;
import com.example.kayseri_ulasim.Model.ModelLineofStops;
import com.example.kayseri_ulasim.Model.ModelLinesBetweenStops;
import com.example.kayseri_ulasim.Model.ModelMesafeOfHat;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBusLinesResponse;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelNewBusLines;
import com.example.kayseri_ulasim.Model.ModelStationList;
import com.example.kayseri_ulasim.Model.ModelStationsResponse;
import com.example.kayseri_ulasim.Model.ModelStopsOfLines;
import com.example.kayseri_ulasim.Model.ModelToken;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RestMethods {
  
  public static void ofServerInternet(final InterfaceServerStatus callBack) {
	 RetroBaseURL.getmyApi().getInternetStatus().enqueue(new Callback<ModelCheckInternet>() {
		@Override
		public void onResponse(Call<ModelCheckInternet> call, Response<ModelCheckInternet> response) {
		  
		  if (response.body() != null) {
			 if (callBack != null && response.body().getAvaible().equals("var")) {
				callBack.onResponse(true);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelCheckInternet> call, Throwable t) {
		  
		  if (callBack != null)
			 callBack.onResponse(false);
		}
	 });
  }
  
  public static void fetchTokenValue(final InterfaceTokenValue callBack) {
	 RetroBaseURL.getmyApi().getTokenValue().enqueue(new Callback<ModelToken>() {
		@Override
		public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {
		  if (response.body() != null) {
			 if (callBack != null && response.body().getToken() != null) {
				callBack.onResponse(response.body().getToken());
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelToken> call, Throwable t) {
		  callBack.onResponse("duv/gg+GiLqcvVwqkhuJw7oeSmBjZTy0RYmLRDXDBbjowIn4ylfQUeeMAiH+ziJo");
		}
	 });
	 
  }
  
  public static void fetchNearbyStations(Double lat, Double lng, int count, final InterfaceNearbyStations callBack) {
	 
	 RetroBaseURL.getmyApi().getNearbyStations(lat, lng, count).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("success")) {
				if (response.body().getStations() != null) {
				  List<ModelStationList> stationLists = response.body().getStations();
				  callBack.onResponse("success", stationLists);
				}
			 } else if (response.body().getError_msg().equals("empty")) {
				callBack.onResponse("empty", null);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  callBack.onResponse("failure", null);
		}
	 });
	 
  }
  
  //Durakı adı girilerek hat getirir
  public static void fetchBusLines(String bus_no, final InterfaceBusLines callBack) {
	 RetroBaseURL.getmyApi().getLinesOfStops(bus_no).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("success")) {
				if (response.body().getLineofStops() != null) {
				  List<ModelLineofStops> modelLinesOfStops = response.body().getLineofStops();
				  callBack.onResponse("success", modelLinesOfStops);
				}
			 } else if (response.body().getError_msg().equals("empty")) {
				callBack.onResponse("empty", null);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  callBack.onResponse("failure", null);
		}
	 });
  }
  
  //Hat ararken kullanılan interface
  public static void fetchSearchedLinesOfStops(String text, final InterfaceLinesOfStops callBack) {
	 RetroBaseURL.getmyApi().getBusLines(text, text).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("success")) {
				if (response.body().getLineofStops() != null) {
				  List<ModelLineofStops> linesOfStops = response.body().getLineofStops();
				  callBack.onResponse("success", linesOfStops);
				}
			 } else if (response.body().getError_msg().equals("empty")) {
				callBack.onResponse("empty", null);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  callBack.onResponse("failure", null);
		}
	 });
  }
  
  
  public static void fetchStopsOfBusLines(int hatid, final InterfaceStopsOfLines callBack) {
	 
	 RetroBaseURL.getmyApi().getStopsOfLines(hatid).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("success")) {
				if (response.body().getStopsOfLines() != null) {
				  List<ModelStopsOfLines> stopsOfLines = response.body().getStopsOfLines();
				  callBack.onResponse("success", stopsOfLines);
				}
			 } else if (response.body().getError_msg().equals("empty")) {
				callBack.onResponse("empty", null);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  callBack.onResponse("failure", null);
		}
	 });
  }
  
  public static void fetchHowToGo(Double begin_lat, Double begin_lng, Double end_lat, Double end_lng, int count1, int count2, final InterfaceHowToGo callBack) {
	 
	 RetroBaseURL.getmyApi().getHowToGo(begin_lat, begin_lng, end_lat, end_lng, count1, count2).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null) {
			 if (response.body().getError_msg().equals("success")) {
				if (response.body().getHowtogo() != null) {
				  List<ModelHowToGo> modelHowToGos = response.body().getHowtogo();
				  callBack.onResponse("success", modelHowToGos);
				}
			 } else if (response.body().getError_msg().equals("empty")) {
				callBack.onResponse("empty", null);
			 }
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		  callBack.onResponse("failure", null);
		}
	 });
  }
  
  public static void getMesafeOfHat(int hatid, int beginStop, int endStop, final InterFaceMesafeOfHat callBack) {
	 
	 RetroBaseURL.getmyApi().getMesafeOfHat(hatid, beginStop, endStop).enqueue(new Callback<ModelMesafeOfHat>() {
		@Override
		public void onResponse(Call<ModelMesafeOfHat> call, Response<ModelMesafeOfHat> response) {
		  
		  if (response.body() != null) {
			 callBack.onResponse(response.body().getMesafe());
		  }
		  
		}
		
		@Override
		public void onFailure(Call<ModelMesafeOfHat> call, Throwable t) {
		
		}
	 });
  }
  
  public static void getLinesBetweenStops(int hatid, int beginStop, int endStop, final InterfaceLBetweenS callBack) {
	 RetroBaseURL.getmyApi().getLBetweenS(hatid, beginStop, endStop).enqueue(new Callback<ModelStationsResponse>() {
		@Override
		public void onResponse(Call<ModelStationsResponse> call, Response<ModelStationsResponse> response) {
		  
		  if (response.body() != null && response.body().getLinesBetweenStops() != null) {
			 List<ModelLinesBetweenStops> betweenStops = response.body().getLinesBetweenStops();
			 callBack.onResponse("success", betweenStops);
		  }
		}
		
		@Override
		public void onFailure(Call<ModelStationsResponse> call, Throwable t) {
		
		}
	 });
  }
  
  public static void fetchTimesOfLines(int busNo, Context mContext, final InterfaceTimesOfLines callBack) {
	 
	 RetroBaseURL2.getmyApi().getTimesOfLines(CallMethods.getTokenValue(mContext), busNo).enqueue(new Callback<ModelBusLinesResponse>() {
		@Override
		public void onResponse(Call<ModelBusLinesResponse> call, Response<ModelBusLinesResponse> response) {
		  if (response.body() != null && response.body().getTimesOfLines() != null) {
			 List<ModelNewBusLines> busLines = response.body().getTimesOfLines();
			 callBack.onResponse("success", busLines);
		  }
		  
		}
		
		@Override
		public void onFailure(Call<ModelBusLinesResponse> call, Throwable t) {
		  
		  callBack.onResponse("failure", null);
		}
	 });
	 
	 
  }
}
