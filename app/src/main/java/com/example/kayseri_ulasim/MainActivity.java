package com.example.kayseri_ulasim;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.droidbyme.dialoglib.DroidDialog;
import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Fragments.AboutFragment;
import com.example.kayseri_ulasim.Fragments.ContactMeFragment;
import com.example.kayseri_ulasim.Fragments.EczaneFragment;
import com.example.kayseri_ulasim.Fragments.HowToGoFragment;
import com.example.kayseri_ulasim.Fragments.MainPageFragment;
import com.example.kayseri_ulasim.Fragments.News.NewsFragment;
import com.example.kayseri_ulasim.Fragments.OtoparkFragment;
import com.example.kayseri_ulasim.Fragments.Stations.StationsFragment;
import com.example.kayseri_ulasim.Fragments.VefatFragment;
import com.example.kayseri_ulasim.Model.ModelUpdateLocation;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import static com.example.kayseri_ulasim.util.Constants.REQUEST_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  
  private DrawerLayout drawerLayout;
  private FusedLocationProviderClient fusedLocation;
  private LocationRequest locationRequest;
  private LocationCallback locationCallback;
  private DroidDialog dialog;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_main);
	 
	 Toolbar toolbar = findViewById(R.id.toolbar);
	 setSupportActionBar(toolbar);
	 
	 drawerLayout = findViewById(R.id.drawer_layout);
	 NavigationView navigationView = findViewById(R.id.nav_view);
	 navigationView.setNavigationItemSelectedListener(this);
	 
	 ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
				R.string.nav_drawer_open, R.string.nav_drawer_close);
	 drawerLayout.addDrawerListener(toggle);
	 toggle.syncState(); //Menü açma ikonunu oluşturur
	 
	 
	 fusedLocation = LocationServices.getFusedLocationProviderClient(MainActivity.this);
	 
	 if (savedInstanceState == null) {
		//Navigation Drawer'ın başlangıçta açacağı pencere
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
				  new MainPageFragment()).commit();
		navigationView.setCheckedItem(R.id.nav_main_page);
	 }
	 
	 make_announcement();
	 
	 //Shared Preferences değerine token değerini ata.
	 RestMethods.fetchTokenValue(token -> {
		CallMethods.setTokenValue(this, token);
		Log.e("LOGE", "mainactivity: " + token);
	 });
  }
  
  private void make_announcement() {
	 SharedPreferences prefs = getSharedPreferences("firstTime", MODE_PRIVATE);
	 
	 if (prefs.getBoolean("firstrun", true)) {
		
		new DroidDialog.Builder(MainActivity.this)
				  .icon(R.drawable.ic_info)
				  .title("Uygulama Tanıtımı")
				  .content("\u2022 Uygulamanın hiçbir resmi geçerliliği yoktur. Bir öğrenci projesidir.\n" +
							 "\u2022 Akıcı bir kullanım için uygulamayı oldukça sade tutmaya çalıştım.\n" +
							 "\u2022 Yanlış veya eksik veriler benim kontrolüm dışında oluşmaktadır.\n" +
							 "\u2022 Uygulama, verilere erişim kaybediline kadar mağazada bulundurulacaktır.")
				  .cancelable(false, false)
				  .positiveButton("Tamam", Dialog::dismiss)
				  .show();
		
		prefs.edit().putBoolean("firstrun", false).apply();
	 }
  }
  
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
	 //Sol menüden item seçildiğinde yapılacak işlemlerin belirlendiği metot
	 
	 switch (menuItem.getItemId()) {
		case R.id.nav_main_page:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new MainPageFragment()).commit();
		  break;
		case R.id.nav_stations:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new StationsFragment()).addToBackStack("mainPage").commit();
		  stopLocationUpdates(); //Eğer durak menüsüne gelirsen, zaten halihazırda request yapılacak. O yüzden bunu kapat
		  break;
		case R.id.nav_haberler:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new NewsFragment()).addToBackStack("mainPage").commit();
		  break;
		
		case R.id.nav_nasil_giderim:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new HowToGoFragment()).addToBackStack("mainPage").commit();
		  break;
		
		case R.id.nav_eczaneler:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new EczaneFragment()).addToBackStack("mainPage").commit();
		  break;
		
		case R.id.nav_vefat:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new VefatFragment()).addToBackStack("mainPage").commit();
		  break;
		
		case R.id.nav_otopark:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new OtoparkFragment()).addToBackStack("mainPage").commit();
		  break;
		
		//	case R.id.nav_ayarlar:
		
		//	  break;
		
		case R.id.nav_bize_ulas:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new ContactMeFragment()).addToBackStack("mainPage").commit();
		  break;
		case R.id.nav_hakkinda:
		  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					 new AboutFragment()).addToBackStack("mainPage").commit();
		  break;
	 }
	 //Drawer layout'ta bir item seçeceğimiz için. Drawer Layout'u kapatıyoruz.
	 drawerLayout.closeDrawer(GravityCompat.START);
	 return true;
  }
  
  
  private void buildLocationRequest() {
	 //FusedLocationProviderApi'den lokasyon bilgisi istemek için kullanacağız
	 locationRequest = new LocationRequest();
	 locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	 locationRequest.setInterval(5000); //Benim konum için istekte bulunacağım süre aralığı
	 locationRequest.setFastestInterval(3000); //Başka uygulamalardan alacağım son konum bilgisinin süre aralığı
	 locationRequest.setSmallestDisplacement(30); //konumlar arası metre farkı 40'den fazla ise yeni konum getir.
	 
  }
  
  private void buildLocationCallBack() {
	 
	 locationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
		  for (Location location : locationResult.getLocations()) {
			 //Buradaki amaç bu işlem çalışsınki kullanıcı duraklar menüsüne gitmeden önce
			 //en doğru konumu bulmak için şimdiden konum isteğinde bulunsun
			 CallMethods.setCurrentLocation(getApplicationContext(), location.getLatitude(), location.getLongitude());
			 
			 FavoritesRoomDatabase.getInstance(getApplicationContext()).myFavoritesDao().deleteUpdateLocation();
			 FavoritesRoomDatabase.getInstance(getApplicationContext()).myFavoritesDao()
						.addNewLocation(new ModelUpdateLocation(location.getLatitude(), location.getLongitude()));
		  }
		  
		}
	 };
  }
  
  private void startLocationUpdates() {
	 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
	 }
  }
  
  private void stopLocationUpdates() {
	 if (fusedLocation != null)
		fusedLocation.removeLocationUpdates(locationCallback);
  }
  
  
  private void getPermission() {
	 
	 if (dialog != null) {
		dialog.dismiss();
		dialog = null;
	 }
	 
	 buildLocationRequest();
	 buildLocationCallBack();
	 
	 RestMethods.ofServerInternet(success -> {
		if (success) {
		  if (CallMethods.ofGPS(MainActivity.this)) //eğer gps açıksa
		  {
			 if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
			 dialog = new DroidDialog.Builder(MainActivity.this)
						.icon(R.drawable.ic_action_close)
						.title("GPS Kapalı!")
						.content("GPS bağlantınız kapalı. Lütfen bağlantınızı aktif hale getiriniz.")
						.cancelable(false, false)
						.positiveButton("Tekrar Dene", dialog -> {
						  dialog.dismiss();
						  getPermission();
						})
						.negativeButton("Çıkış", dialog -> {
						  dialog.dismiss();
						  finish();
						})
						.show();
		  }
		} else //eğer internet yoksa
		{
		  dialog = new DroidDialog.Builder(MainActivity.this)
					 .icon(R.drawable.ic_action_close)
					 .title("Sunucuya Bağlanılamadı!")
					 .content("Lütfen internet bağlantınızı aktif hale getirdikten sonra tekrar deneyiniz.")
					 .cancelable(false, false)
					 .positiveButton("Tekrar Dene", dialog -> {
						dialog.dismiss();
						getPermission();
					 })
					 .negativeButton("Çıkış", dialog -> {
						dialog.dismiss();
						finish();
					 })
					 .show();
		  
		}
	 });
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	 
	 if (requestCode == REQUEST_FINE_LOCATION) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
		  //Eğer gps'i sonradan kabul ederse direk verileri yüklemesine izin ver
		  getPermission();
		} else {
		  dialog = new DroidDialog.Builder(MainActivity.this)
					 //.icon(R.drawable.ic_cancel)
					 .animation(8)
					 .title("İzin Vermediniz !")
					 .content("Uygulamanın çalışabilmesi için konum izni gereklidir!")
					 .cancelable(false, false)
					 .positiveButton("Tekrar Dene", dialog -> {
						dialog.dismiss();
						getPermission();
					 })
					 .negativeButton("Çıkış", dialog -> {
						dialog.dismiss();
						finish();
					 })
					 .show();
		}
	 }
  }
  
  @Override
  protected void onResume() {
	 super.onResume();
	 getPermission();
  }
  
  @Override
  protected void onPause() {
	 super.onPause();
	 
	 stopLocationUpdates();
	 
	 //Alt-Tab yaptığında dialog tekrar açılacak ve dialog already open hatası verip uygulama çökecek.
	 if (dialog != null) {
		dialog.dismiss();
		dialog = null;
	 }
	 
  }
  
  @Override
  public void onDestroy() {
	 super.onDestroy();
	 if (dialog != null) {
		dialog.dismiss();
		dialog = null;
	 }
  }
  
  @Override
  public void onBackPressed() {
	 if (drawerLayout.isDrawerOpen(GravityCompat.START)) { //Eğer drawer açıksa geri tuşuna bastığımda drawer'i kapat.
		drawerLayout.closeDrawer(GravityCompat.START);
	 } else {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
		  getSupportFragmentManager().popBackStack();
		} else {
		  super.onBackPressed();
		}
	 }
	 
  }
  
}