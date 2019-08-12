package com.example.kayseri_ulasim.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Adapters.EczaneAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelEczane;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelEczaneResponse;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EczaneFragment extends Fragment {
  
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private Context mContext;
  private View mView;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 mView = inflater.inflate(R.layout.fragment_eczane, container, false);
	 
	 initializeWidgets();
	 
	 getEczaneValues();
	 
	 return mView;
  }
  
  private void getEczaneValues() {
	 RetroBaseURL2.getmyApi().getEczaneValues(CallMethods.getTokenValue(mContext)).enqueue(new Callback<ModelEczaneResponse>() {
		@Override
		public void onResponse(Call<ModelEczaneResponse> call, Response<ModelEczaneResponse> response) {
		  
		  if (response.body() != null) {
			 List<ModelEczane> modelEczanes = response.body().getModelEczanes();
			 List<ModelEczane> modelEczanesByDistance = new ArrayList<>();
			 for (int i = 0; i < modelEczanes.size(); i++) {
				String lat = modelEczanes.get(i).getLatitude();
				String lng = modelEczanes.get(i).getLongitude();
				
				if (lat.length() != 0 && lng.length() != 0) {
				  modelEczanesByDistance.add(new ModelEczane(modelEczanes.get(i).getName(),
							 modelEczanes.get(i).getNobet_turu(), modelEczanes.get(i).getAdres(),
							 modelEczanes.get(i).getPhone_number(), modelEczanes.get(i).getIlce(),
							 modelEczanes.get(i).getLatitude(), modelEczanes.get(i).getLongitude(),
							 CallMethods.distanceFromMe(mContext, Double.parseDouble(modelEczanes.get(i).getLatitude()),
										Double.parseDouble(modelEczanes.get(i).getLongitude()))));
				} else {
				  modelEczanesByDistance.add(new ModelEczane(modelEczanes.get(i).getName(),
							 modelEczanes.get(i).getNobet_turu(), modelEczanes.get(i).getAdres(),
							 modelEczanes.get(i).getPhone_number(), modelEczanes.get(i).getIlce(),
							 modelEczanes.get(i).getLatitude(), modelEczanes.get(i).getLongitude(), 0.0));
				}
				
			 }
			 
			 Collections.sort(modelEczanesByDistance, (c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()));
			 
			 if (modelEczanes.isEmpty()) {
				CallMethods.createSnackBar(mView, "Seçili güne ait herhangi bir veri bulunamadı", 10).show();
			 } else {
				recyclerView.setAdapter(new EczaneAdapter(mContext, modelEczanesByDistance));
			 }
			 progressBar.setVisibility(View.GONE);
		  } else {
			 CallMethods.createSnackBar(mView, getString(R.string.noDataTaken), 2).show();
			 progressBar.setVisibility(View.GONE);
		  }
		  
		  
		}
		
		@Override
		public void onFailure(Call<ModelEczaneResponse> call, Throwable t) {
		  CallMethods.createSnackBar(mView, getString(R.string.noDataTaken), 2).show();
		  progressBar.setVisibility(View.GONE);
		}
	 });
  }
  
  
  private void initializeWidgets() {
	 recyclerView = mView.findViewById(R.id.recycler_eczane);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 
	 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL);
	 //  dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.recyclerdivider));
	 recyclerView.addItemDecoration(dividerItemDecoration);
	 
	 progressBar = mView.findViewById(R.id.progressBar_eczane);
	 
  }
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}
