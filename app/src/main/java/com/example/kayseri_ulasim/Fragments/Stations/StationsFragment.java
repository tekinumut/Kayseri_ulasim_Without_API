package com.example.kayseri_ulasim.Fragments.Stations;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.droidbyme.dialoglib.DroidDialog;
import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Fragments.News.NewsFragment;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.Model.ModelUpdateLocation;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.kayseri_ulasim.util.Constants.REQUEST_FINE_LOCATION;

public class StationsFragment extends Fragment {
  
  private Context mContext;
  private DroidDialog dialog;
  private FusedLocationProviderClient fusedLocation;
  private LocationRequest locationRequest;
  private LocationCallback locationCallback;
  private ViewPager viewPager;
  private TabLayout tabLayout;
  private ProgressBar progressBar;
  private View rootView;
  
  FragmentManager fragmentManager;
  
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 rootView = inflater.inflate(R.layout.fragment_stations, container, false);
	 
	 fragmentManager = getChildFragmentManager();
	 
	 viewPager = rootView.findViewById(R.id.viewpager);
	 viewPager.setOffscreenPageLimit(3);
	 tabLayout = rootView.findViewById(R.id.stations_tabs);
	 progressBar = rootView.findViewById(R.id.progressBar_stations);
	 
	 fusedLocation = LocationServices.getFusedLocationProviderClient(mContext);
	 
	 getLocation();
	 
	 //Telefon dönünce layoutu ve datayı koru.
	 setRetainInstance(true);
  
	 
	 return rootView;
  }
  
  private void getLocation() {
	 
	 buildLocationRequest();
	 buildLocationCallBack();
	 
	 RestMethods.ofServerInternet(success -> {
		if (success) {
		  
		  if (CallMethods.ofGPS(mContext)) //eğer gps açıksa
		  {
			 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				//eğer izin alınmışsa işlemleri başlat
				startLocationUpdates();
				
			 } else {//Eğer izin alınmamış ise
				
				//izin iste request permission ile
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				  requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
			 }
		  } else //eğer gps kapalı ise
		  {
			 //try
			 dialog = new DroidDialog.Builder(mContext)
						.icon(R.drawable.ic_action_close)
						.title("GPS Kapalı!")
						.content("GPS bağlantınız kapalı. Lütfen bağlantınızı aktif hale getiriniz.")
						.cancelable(false, false)
						.positiveButton("Tamam", Dialog::dismiss)
						.show();
		  }
		} else //eğer internet yoksa
		{
		  dialog = new DroidDialog.Builder(mContext)
					 .icon(R.drawable.ic_action_close)
					 .title("Sunucuya Bağlanılamadı!")
					 .content("Lütfen internet bağlantınızı aktif hale getirdikten sonra tekrar deneyiniz.")
					 .cancelable(false, false)
					 .positiveButton("Tamam", Dialog::dismiss)
					 .show();
		  
		}
	 });
	 
  }
  
  private void buildLocationRequest() {
	 locationRequest = new LocationRequest();
	 locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	 locationRequest.setInterval(1000 * 5);
	 locationRequest.setFastestInterval(1000 * 3);
	 locationRequest.setSmallestDisplacement(40);//konumlar arası metre farkı 40m'den fazla ise yeni konum getir.
	 
  }
  
  private void buildLocationCallBack() { //Konum her değiştiğinde
	 
	 locationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
		  //Her 40 metrelik değişimde konumu yeniden gönder.
		  for (Location location : locationResult.getLocations()) {
			 
			 CallMethods.setCurrentLocation(mContext, location.getLatitude(), location.getLongitude());
			 
			 //Eğer ilk kez yükleniyorsa sadece viewpager'ı yükle
			 if (viewPager.getAdapter() == null) {
				setupViewPager(viewPager, location.getLatitude(), location.getLongitude(), viewPager.getCurrentItem());
				tabLayout.setupWithViewPager(viewPager);
				//Eğer viewpager yüklendikten sonra konum 40metreden fazla değişirse
				//viewpager'ı tekrar yüklenip yüklenmeyeceğini soran bir snackbar aç.
				//snackbar zaten açıksa yeniden açma.
			 } else {
				FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().deleteUpdateLocation();
				FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao()
						  .addNewLocation(new ModelUpdateLocation(location.getLatitude(), location.getLongitude()));
				Toast.makeText(mContext, "Konumunuz değişti.", Toast.LENGTH_SHORT).show();
				
			 }
			 
			 
		  } //end of FOR
		  
		}
	 };
	 
  }
  
  private void startLocationUpdates() {
	 if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
	 }
  }
  
  private void stopLocationUpdates() {
	 if (fusedLocation != null)
		fusedLocation.removeLocationUpdates(locationCallback);
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	 
	 if (requestCode == REQUEST_FINE_LOCATION) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
		  //Eğer gps'i sonradan kabul ederse direk verileri yüklemesine izin ver
		  getLocation();
		} else {
		  CallMethods.createSnackBar(rootView, "İlgili bölüme erişmek için için konum izni vermeniz gerekiyor!", 60).show();
		  ((MainActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new NewsFragment()).commit();
		}
	 }
  }
  
  private void setupViewPager(ViewPager viewPager, Double latitude, Double longitude, int currentItem) {
	 
	 Adapter adapter = new Adapter(fragmentManager, latitude, longitude);
	 adapter.addFragment(new Tab1StationsPager(), "Duraklar");
	 adapter.addFragment(new Tab2MapPager(), "Harita");
	 adapter.addFragment(new Tab3BusLines(), "Hatlar");
	 viewPager.setAdapter(adapter);
	 viewPager.setCurrentItem(currentItem);//ViewPager yenilendikçe aynı page'de kalır. Page 2'de iken 1'e atlamaz. veya tersi
	 progressBar.setVisibility(View.GONE); //Viewpager'ı çağırdıktan sonra progressBar'ı kapat.
  }
  
  
  private class Adapter extends FragmentPagerAdapter {//veri güncellenmesi için statePager kullandık
	 private final List<Fragment> mFragmentList = new ArrayList<>();
	 private final List<String> mFragmentTitleList = new ArrayList<>();
	 private Double latitude, longitude;
	 
	 Adapter(FragmentManager manager, Double latitude, Double longitude) {
		super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
		this.latitude = latitude;
		this.longitude = longitude;
	 }
	 
	 
	 @Override
	 public Fragment getItem(int position) {
		Bundle bundle = new Bundle();
		bundle.putDouble(getString(R.string.bundle_latitude), latitude);
		bundle.putDouble(getString(R.string.bundle_longitude), longitude);
		
		switch (position) {
		  case 0:
			 Tab1StationsPager tab1 = new Tab1StationsPager();
			 tab1.setArguments(bundle);
			 return tab1;
		  case 1:
			 Tab2MapPager tab2 = new Tab2MapPager();
			 tab2.setArguments(bundle);
			 return tab2;
		  case 2:
			 Tab3BusLines tab3 = new Tab3BusLines();
			 tab3.setArguments(bundle);
			 return tab3;
		  default:
			 throw new IllegalStateException("Sayfa yüklenirken bir hata meydana geldi.");
		}
		
	 }
	 
	 @Override
	 public int getCount() {
		return mFragmentList.size();
	 }
	 
	 void addFragment(Fragment fragment, String title) {
		mFragmentList.add(fragment);
		mFragmentTitleList.add(title);
	 }
	 
	 @Override
	 public CharSequence getPageTitle(int position) {
		return mFragmentTitleList.get(position);
	 }
  }
  
  
  @Override
  public void onPause() {
	 super.onPause();
	 
	 stopLocationUpdates();
	 
	 if (dialog != null)
		dialog.dismiss();
  }
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
  
}
