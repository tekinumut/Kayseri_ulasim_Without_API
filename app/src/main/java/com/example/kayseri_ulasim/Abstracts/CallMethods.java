package com.example.kayseri_ulasim.Abstracts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.kayseri_ulasim.AlertDialogs.AlertBusLines;
import com.example.kayseri_ulasim.AlertDialogs.AlertChooseDirection;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public abstract class CallMethods {
  
  public static boolean ofGPS(Context mContext) {
	 
	 LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	 
	 return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }
  
  
  public static boolean imInFavorites(Context mContext) {
	 SharedPreferences amIinFavorites = mContext.getSharedPreferences(mContext.getString(R.string.booleanFavorites), Context.MODE_PRIVATE);
	 
	 return amIinFavorites.getBoolean("favorite", false);
  }
  
  public static OkHttpClient setTimeout(int second) {
	 return new OkHttpClient.Builder()
				.connectTimeout(second, TimeUnit.SECONDS)
				.readTimeout(second, TimeUnit.SECONDS)
				.writeTimeout(second, TimeUnit.SECONDS)
				.build();
  }
  
  
  //StationsAdapter hepsi açık
  //AlertMarkerInfoPanel konumu göster kapalı
  //bus no yok ise -1 kullan
  public static void getChooseDirectionsDialog(Context mContext, String whereYouFrom, String name, String snippet, Double latitude, Double longitude) {
	 //Yol tarifi seçimi için seçim penceresini aç
	 FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
	 Bundle bundle = new Bundle();
	 AlertChooseDirection chooseDirection = new AlertChooseDirection();
	 
	 bundle.putString(mContext.getString(R.string.bundle_whereyoufrom), whereYouFrom);
	 bundle.putString(mContext.getString(R.string.bundle_name), name);
	 bundle.putString(mContext.getString(R.string.bundle_snippet), snippet);
	 bundle.putDouble(mContext.getString(R.string.bundle_latitude), latitude);
	 bundle.putDouble(mContext.getString(R.string.bundle_longitude), longitude);
	 
	 chooseDirection.setArguments(bundle);
	 chooseDirection.show(manager, null);
  }
  
  // if where =0 ==>gececekotobusler
  // if =1 ==> gecenhatlar
  public static void getBusLineDialog(Context mContext, int bus_no, int where_busline) {
	 FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
	 AlertBusLines alertBusLines = new AlertBusLines();
	 Bundle bundle = new Bundle();
	 
	 bundle.putInt(mContext.getString(R.string.bundle_bus_no), bus_no);
	 bundle.putInt(mContext.getString(R.string.bundle_where_busline), where_busline);
	 
	 alertBusLines.setArguments(bundle);
	 alertBusLines.show(manager, null);
	 
  }
  
  
  public static void getDirectionFromMaps(Context mContext, Double latitude, Double longitude, String mTitle) {
	 String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + mTitle + ")";
	 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
	 mContext.startActivity(intent);
  }
  
  public static void getDirectionFromNavigation(Context mContext, Double latitude, Double longitude) {
	 Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
	 Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
	 mapIntent.setPackage("com.google.android.apps.maps");
	 mContext.startActivity(mapIntent);
  }
  
  public static String yourDistanceAsText(Double distance) {
	 NumberFormat formatter = new DecimalFormat("#0");
	 if (distance < 1000) {
		return formatter.format(distance) + " m";
	 } else {
		formatter = new DecimalFormat("#0.00");
		return formatter.format(distance / 1000) + " km";
	 }
  }
  
  public static Snackbar createSnackBar(View view, String text, int second) {
	 if (view != null) {
		Snackbar snackbar = Snackbar.make(view, text, second * 1000);
		if (second > 15) { //Eğer snackbarın görüneceği süre 15 saniyeden fazla ise 'Tamam' butonunu göster
		  snackbar.setAction("Tamam", v -> {
			 //Tamama tıklayınca bir işlem yap. Undo gibi
		  }).setActionTextColor(Color.parseColor("#2980b9"));
		  return snackbar;
		} else {
		  return snackbar;
		}
	 } else {
		return null;
	 }
	 
  }
  
  public static String toEnglish(String text) {
	 
	 //Büyük türkçe karakter yazarsa onları Büyük İngilizce harfe çevir
	 return text.replace("Ç", "C").replace("İ", "I").replace("Ğ", "G")
				.replace("Ö", "O").replace("Ş", "S").replace("Ü", "U")
				//küçük türkçe karakter yazarsa onları çevirir
				.replace("ç", "C").replace("i", "I").replace("ğ", "G")
				.replace("ö", "O").replace("ş", "S").replace("ü", "U")
				//Küçük ingilizce karakterleri çevirir
				.replace("c", "C").replace("ı", "I").replace("g", "G")
				.replace("o", "O").replace("s", "S").replace("u", "U");
  }
  
  public static void setCurrentLocation(Context mContext, Double latitude, Double longitude) {
	 SharedPreferences locationPref = mContext.getSharedPreferences("currentlocation", Context.MODE_PRIVATE);
	 SharedPreferences.Editor locationPrefEdit = locationPref.edit();
	 
	 locationPrefEdit.putString("latitude", "" + latitude).apply();
	 locationPrefEdit.putString("longitude", "" + longitude).apply();
	 
  }
  
  public static void setTokenValue(Context mContext, String token) {
	 SharedPreferences preferences = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
	 SharedPreferences.Editor locationPrefEdit = preferences.edit();
	 
	 if (!token.isEmpty()) {
		locationPrefEdit.putString("tokenValue", token).apply();
	 } else {
		locationPrefEdit.putString("tokenValue", "duv/gg+GiLqcvVwqkhuJw7oeSmBjZTy0RYmLRDXDBbjowIn4ylfQUeeMAiH+ziJo").apply();
	 }
	 
	 
  }
  
  public static String getTokenValue(Context mContext) {
	 SharedPreferences preferences = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
	 return preferences.getString("tokenValue", "duv/gg+GiLqcvVwqkhuJw7oeSmBjZTy0RYmLRDXDBbjowIn4ylfQUeeMAiH+ziJo");
	 
  }
  
  public static LatLng getCurrentLocation(Context mContext) {
	 SharedPreferences locationPref = mContext.getSharedPreferences("currentlocation", Context.MODE_PRIVATE);
	 
	 return new LatLng(Double.parseDouble(locationPref.getString("latitude", "0.0")),
				Double.parseDouble(locationPref.getString("longitude", "0.0")));
  }
  
  public static SpannableString spanColorBlackText(String text, int start, int end) {
	 
	 SpannableString ss = new SpannableString(text);
	 
	 ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, 0);
	 
	 return ss;
	 
  }
  
  /*
  public static void hideKeyboard(Context mContext) //Klavye gizleme metodu. Fragment için
	 {
		View view = ((MainActivity) mContext).getCurrentFocus();
		if (view != null) {
		  InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	 }
	 */
  
  
  public static Double distanceFromMe(Context mContext, Double lat2, Double long2) {
	 LatLng latLng = CallMethods.getCurrentLocation(mContext);
	 
	 Location loc1 = new Location("");
	 loc1.setLatitude(latLng.latitude);
	 loc1.setLongitude(latLng.longitude);
	 
	 Location loc2 = new Location("");
	 loc2.setLatitude(lat2);
	 loc2.setLongitude(long2);
	 
	 Float distance = loc1.distanceTo(loc2);
	 
	 return distance.doubleValue();
	 
  }
  
  
}
