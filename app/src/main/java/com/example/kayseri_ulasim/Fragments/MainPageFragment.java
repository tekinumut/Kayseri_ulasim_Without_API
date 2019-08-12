package com.example.kayseri_ulasim.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.BusLinesAdapter;
import com.example.kayseri_ulasim.Fragments.News.NewsFragment;
import com.example.kayseri_ulasim.Fragments.Stations.StationsFragment;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.FavoritesRoomDatabase;

public class MainPageFragment extends Fragment {
  
  private TextView ulasim, eczane, otopark, bisiklet, vefat, haberler, nasil_giderim;
  private ProgressBar progressBar;
  private View mView;
  private Context mContext;
  private ViewGroup container1;
  private FragmentManager fragmentManager;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 mView = inflater.inflate(R.layout.fragment_main_page, container, false);
	 container1 = container;
	 fragmentManager = getFragmentManager();
	 
	 initializeWidgets();
	 
	 clickListeners();
	 
	 fetchNearbyStations();
	 
	 return mView;
  }
  
  private void initializeWidgets() {
	 ulasim = mView.findViewById(R.id.main_ulasim);
	 eczane = mView.findViewById(R.id.main_eczane);
	 otopark = mView.findViewById(R.id.main_otopark);
	 bisiklet = mView.findViewById(R.id.main_bisiklet);
	 vefat = mView.findViewById(R.id.main_vefat);
	 haberler = mView.findViewById(R.id.main_haberler);
	 nasil_giderim = mView.findViewById(R.id.main_nasil_giderim);
	 
	 progressBar = mView.findViewById(R.id.progressBar_main_fragment);
	 
  }
  
  
  private void openFragment(Fragment newFragment, String tag) {
	 fragmentManager.beginTransaction()
				.replace(container1.getId(), newFragment, tag)
				.addToBackStack("mainPage")
				.commit();
  }
  
  //Burada en yakın durağı al ve geçen hatları ana sayfaya yaz
  private void fetchNearbyStations() {
	 
	 TextView emptyText = mView.findViewById(R.id.main_emptyText);
	 TextView durak_no_text = mView.findViewById(R.id.main_durak);
	 
	 RecyclerView recyclerView = mView.findViewById(R.id.recycler_main_busline);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 
	 FavoritesRoomDatabase.getInstance(mContext).myFavoritesDao().getLocation().observe(MainPageFragment.this, model -> {
		
		//Eğer veritabanında veri varsa
		if (model.size() > 0) {
		  Double latitude = model.get(0).getLatitude();
		  Double longitude = model.get(0).getLongitude();
		  
		  RestMethods.fetchNearbyStations(latitude, longitude, 20, (situation, modelStationList) -> {
			 
			 switch (situation) {
				case "empty":
				  emptyText.setVisibility(View.VISIBLE);
				  break;
				case "success":
				  int bus_no = modelStationList.get(0).getId();
				  
				  //Duraktan geçen hatları getir.
				  
				  RestMethods.fetchTimesOfLines(bus_no, mContext, (situation1, modelBusLines) -> {
					 if (situation1.equals("success")) {
						if (modelBusLines.size() == 0) {
						  recyclerView.setVisibility(View.GONE);
						  emptyText.setVisibility(View.VISIBLE);
						  progressBar.setVisibility(View.GONE);
						  durak_no_text.setVisibility(View.INVISIBLE);
						} else {
						  //EĞER HER ŞEY SORUNSUZ İSE
						  recyclerView.setAdapter(new BusLinesAdapter(mContext, modelBusLines));
						  progressBar.setVisibility(View.GONE);
						  emptyText.setVisibility(View.GONE);
						  recyclerView.setVisibility(View.VISIBLE);
						  durak_no_text.setVisibility(View.VISIBLE);
						  durak_no_text.setText("Durak No: " + bus_no);
						}
					 } else if (situation1.equals("failure")) {
						emptyText.setVisibility(View.VISIBLE);
						durak_no_text.setVisibility(View.INVISIBLE);
					 }
					 
				  });
				  break;
				case "failure":
				  emptyText.setVisibility(View.VISIBLE);
				  durak_no_text.setVisibility(View.INVISIBLE);
				  break;
			 }
			 progressBar.setVisibility(View.GONE);
		  });
		  
		}
		//eğer room veritabanında veri yok ise
		else {
		  emptyText.setVisibility(View.VISIBLE);
		  progressBar.setVisibility(View.GONE);
		}
		
	 });//observe and döngünün sonu

//	 progressBar.setVisibility(View.GONE);
	 
  }
  
  
  private void clickListeners() {
	 
	 ulasim.setOnClickListener(v -> {
		//
		openFragment(new StationsFragment(), "Stations");
	 });
	 nasil_giderim.setOnClickListener(v -> {
		//
		openFragment(new HowToGoFragment(), "HowToGo");
	 });
	 eczane.setOnClickListener(v -> {
		//
		openFragment(new EczaneFragment(), "Eczane");
	 });
	 otopark.setOnClickListener(v -> {
		//
		openFragment(new OtoparkFragment(), "Otopark");
	 });
	 bisiklet.setOnClickListener(v -> {
		//
		openFragment(new BisikletFragment(), "Bisiklet");
	 });
	 vefat.setOnClickListener(v -> {
		//
		openFragment(new VefatFragment(), "Vefat");
	 });
	 haberler.setOnClickListener(v -> {
		//
		openFragment(new NewsFragment(), "Haberler");
	 });
  }
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}
