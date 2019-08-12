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
import com.example.kayseri_ulasim.Adapters.BisikletAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelBisiklet;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL3;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BisikletFragment extends Fragment {
  
  private Context mContext;
  private View mView;
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private Button all_bisiklet;
  private FragmentManager fragmentManager;
  private List<ModelBisiklet> modelBisiklets;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 mView = inflater.inflate(R.layout.fragment_bisiklet, container, false);
	 fragmentManager = getFragmentManager();
	 
	 init();
	 
	 getBisikletValues();
	 
	 showAllOtoparks(container);
	 
	 
	 return mView;
  }
  
  private void init() {
	 recyclerView = mView.findViewById(R.id.rec_view_bisiklet);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 
	 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL);
	 //  dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.recyclerdivider));
	 recyclerView.addItemDecoration(dividerItemDecoration);
	 
	 progressBar = mView.findViewById(R.id.progressBar_bisiklet);
	 all_bisiklet = mView.findViewById(R.id.bisiklet_show_on_map);
  }
  
  private void showAllOtoparks(ViewGroup container) {
	 
	 all_bisiklet.setOnClickListener(v -> {
		
		//fragment gönder
		BisikletMarkersFragment fragment = new BisikletMarkersFragment();
		Bundle bundle = new Bundle();
		
		//Zaten hesaplattık
		String jsonValue = new Gson().toJson(modelBisiklets);
		
		bundle.putString("values", jsonValue);
		
		fragment.setArguments(bundle);
		
		fragmentManager.beginTransaction()
				  .add(container.getId(), fragment, "Bisiklet")
				  .addToBackStack("AllBisiklets")
				  .commit();
	 });
	 
  }
  
  
  private void getBisikletValues() {
	 
	 RetroBaseURL3.getmyApi().getBisikletValues().enqueue(new Callback<List<ModelBisiklet>>() {
		@Override
		public void onResponse(Call<List<ModelBisiklet>> call, Response<List<ModelBisiklet>> response) {
		  
		  if (response.body() != null) {
			 List<ModelBisiklet> modelList = response.body();
			 modelBisiklets = new ArrayList<>();
			 
			 for (int i = 0; i < modelList.size(); i++) {
				
				String lat = modelList.get(i).getLat();
				String lng = modelList.get(i).getLng();
				
				if (lat.length() > 0 && lng.length() > 0) {
				  modelBisiklets.add(new ModelBisiklet(modelList.get(i).getDurakAdi(), modelList.get(i).getDoluSlot(), modelList.get(i).getBosSlot(),
							 modelList.get(i).getLat(), modelList.get(i).getLng(),
							 CallMethods.distanceFromMe(mContext, Double.parseDouble(modelList.get(i).getLat()), Double.parseDouble(modelList.get(i).getLng()))
				  ));
				}
			 }
			 Collections.sort(modelBisiklets, (c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()));
			 if (modelBisiklets.isEmpty()) {
				CallMethods.createSnackBar(mView, "Veri bulunamadı", 10).show();
			 } else {
				recyclerView.setAdapter(new BisikletAdapter(mContext, modelBisiklets));
			 }
			 
		  } else {
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
		  }
		  
		  progressBar.setVisibility(View.GONE);
		  
		}
		
		@Override
		public void onFailure(Call<List<ModelBisiklet>> call, Throwable t) {
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
