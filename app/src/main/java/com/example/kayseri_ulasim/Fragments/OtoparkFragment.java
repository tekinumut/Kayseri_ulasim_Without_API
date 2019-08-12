package com.example.kayseri_ulasim.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Adapters.OtoparkAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelOtopark;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL3;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtoparkFragment extends Fragment {
  
  private View mView;
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private Context mContext;
  private Button all_otoparks;
  private FragmentManager fragmentManager;
  List<ModelOtopark> otoparksForDistance;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 mView = inflater.inflate(R.layout.fragment_otopark, container, false);
	 fragmentManager = getFragmentManager();
	 
	 initializeWidgets();
	 
	 getOtoparkValues();
	 
	 showAllOtoparks(container);
	 
	 return mView;
  }
  
  
  private void initializeWidgets() {
	 recyclerView = mView.findViewById(R.id.rec_view_otopark);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 
	 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL);
	 //  dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.recyclerdivider));
	 recyclerView.addItemDecoration(dividerItemDecoration);
	 
	 progressBar = mView.findViewById(R.id.progressBar_otopark);
	 all_otoparks = mView.findViewById(R.id.otopark_show_on_map);
	 
	 
  }
  
  private void showAllOtoparks(ViewGroup container) {
	 
	 all_otoparks.setOnClickListener(v -> {
		
		//fragment gönder
		OtoparkMarkersFragment fragment = new OtoparkMarkersFragment();
		Bundle bundle = new Bundle();
		
		//Zaten hesaplattık
		String jsonValue = new Gson().toJson(otoparksForDistance);
		
		bundle.putString("values", jsonValue);
		
		fragment.setArguments(bundle);
		
		fragmentManager.beginTransaction()
				  .replace(container.getId(), fragment, "Otopark")
				  .addToBackStack("AllOtoparks")
				  .commit();
	 });
	 
  }
  
  //Uzaklığa göre sıralıyoruz.
  private void getOtoparkValues() {
	 RetroBaseURL3.getmyApi().getOtoparkValues().enqueue(new Callback<List<ModelOtopark>>() {
		@Override
		public void onResponse(Call<List<ModelOtopark>> call, Response<List<ModelOtopark>> response) {
		  
		  if (response.body() != null) {
			 
			 List<ModelOtopark> modelOtoparks = response.body();
			 otoparksForDistance = new ArrayList<>();
			 
			 for (int i = 0; i < modelOtoparks.size(); i++) {
				
				String lat = modelOtoparks.get(i).getLatitude();
				String lng = modelOtoparks.get(i).getLongitude();
				
				if (lat.length() != 0 && lng.length() != 0) {
				  
				  otoparksForDistance.add(new ModelOtopark(modelOtoparks.get(i).getName(), modelOtoparks.get(i).isKatli(),
							 modelOtoparks.get(i).getLatitude(), modelOtoparks.get(i).getLongitude(),
							 CallMethods.distanceFromMe(mContext, Double.parseDouble(modelOtoparks.get(i).getLatitude()),
										Double.parseDouble(modelOtoparks.get(i).getLongitude()))));
				}
			 }
			 Collections.sort(otoparksForDistance, (c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()));
			 
			 if (modelOtoparks.isEmpty()) {
				CallMethods.createSnackBar(mView, "Veri bulunamadı", 10).show();
			 } else {
				recyclerView.setAdapter(new OtoparkAdapter(mContext, otoparksForDistance));
			 }
			 
		  } else {
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
		  }
		  
		  progressBar.setVisibility(View.GONE);
		  
		}
		
		@Override
		public void onFailure(Call<List<ModelOtopark>> call, Throwable t) {
		  
		  Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
		  progressBar.setVisibility(View.GONE);
		}
	 });
  }
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}
