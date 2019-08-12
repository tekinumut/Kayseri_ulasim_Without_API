package com.example.kayseri_ulasim.Fragments.Stations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.LinesOfStopsAdapter;
import com.example.kayseri_ulasim.R;

public class Tab3BusLines extends Fragment {
  
  private Context mContext;
  private View rootView;
  private Double latitude, longitude;
  private RecyclerView recyclerView;
  private SearchView searchLines;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 rootView = inflater.inflate(R.layout.tab3_stations_buslines, container, false);
	 
	 
	 Bundle bundle = getArguments();
	 if (bundle != null) {
		latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		longitude = bundle.getDouble(getString(R.string.bundle_longitude));
	 }
	 
	 initializeWidgets();
	 
	 fetchLines();
	 setSearchView();
	 return rootView;
  }
  
  private void initializeWidgets() {
	 
	 recyclerView = rootView.findViewById(R.id.recycler_hat_tab3);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
	 searchLines = rootView.findViewById(R.id.searchView_tab3);
  }
  
  //Konuma en yakın 5 durağı getir.
  //  //Bu duraklar daha sonra bu duraklardan geçen hatları RecyclerView'a eklemek için kullanılacak.
  private void fetchLines() {
	 
	 RestMethods.fetchNearbyStations(latitude, longitude, 5, (situation, modelStationList) -> {
		
		switch (situation) {
		  case "empty":
			 Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 StringBuilder ids = new StringBuilder();
			 for (int i = 0; i < modelStationList.size(); i++) {
				ids.append(modelStationList.get(i).getId()).append(",");
				if (i == modelStationList.size() - 1) {
				  fetchBusLines(ids.substring(0, ids.length() - 1));
				}
				
			 }
			 break;
		  case "failure":
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
			 break;
		}
		
	 });
	 
  }
  
  //En yakın 5 durağın sahip olduğu otobüs duraklarını getirecek.
  private void fetchBusLines(String bus_no) {
	 
	 RestMethods.fetchBusLines(bus_no, (situation, modelLinesOfStops) -> {
		
		switch (situation) {
		  case "empty":
			 Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 recyclerView.setAdapter(new LinesOfStopsAdapter(mContext, modelLinesOfStops,null));
			 break;
		  case "failure":
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
			 break;
		}
	 });
  }
  
  private void fetchSearchedLines(String text, boolean warning) {
	 recyclerView.setAdapter(null);
	 RestMethods.fetchSearchedLinesOfStops(text, (situation, modelLinesOfStops) -> {
		
		switch (situation) {
		  case "empty":
			 if (warning)
				Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 recyclerView.setAdapter(new LinesOfStopsAdapter(mContext, modelLinesOfStops,null));
			 break;
		  case "failure":
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
			 break;
		}
	 });
  }
  
  private void setSearchView() {
	 
	 searchLines.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String text) {
		  
		  //Text boş olursa tüm hatları tekrar listele
		  if (text.isEmpty()) {
			 fetchLines();
			 searchLines.clearFocus();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedLines(text, true);
			 searchLines.clearFocus();
		  }
		  
		  return true;
		}
		
		@Override
		public boolean onQueryTextChange(String newText) {
		  //Text boş olursa tüm hatları tekrar listele
		  if (newText.isEmpty()) {
			 fetchLines();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedLines(newText, false);
		  }
		  
		  return true;
		}
	 });
  }
  
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}
