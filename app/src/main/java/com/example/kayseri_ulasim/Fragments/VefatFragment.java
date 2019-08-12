package com.example.kayseri_ulasim.Fragments;

import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Adapters.VefatAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelVefat;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelVefatResponse;
import com.example.kayseri_ulasim.R;
import com.example.kayseri_ulasim.Remote.RetroURLs.RetroBaseURL2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VefatFragment extends Fragment {
  
  private Context mContext;
  private RecyclerView recyclerView;
  private TextView searchTextView;
  private ProgressBar progressBar;
  private View mView;
  
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 mView = inflater.inflate(R.layout.fragment_vefat, container, false);
	 
	 initializeWidgets();
	 
	 initializeVefatValues();
	 
	 return mView;
	 
  }
  
  private void initializeWidgets() {
	 recyclerView = mView.findViewById(R.id.recycler_vefat);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 
	 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL);
	 //  dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.recyclerdivider));
	 recyclerView.addItemDecoration(dividerItemDecoration);
	 
	 progressBar = mView.findViewById(R.id.progressBar_vefat);
	 searchTextView = mView.findViewById(R.id.vefat_search_textView);
	 
  }
  
  private void initializeVefatValues() {
	 
	 Calendar calendar = Calendar.getInstance();
	 
	 int day = calendar.get(Calendar.DAY_OF_MONTH);
	 int month = calendar.get(Calendar.MONTH);
	 int year = calendar.get(Calendar.YEAR);
	 
	 SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
	 
	 Date today = new Date();
	 
	 searchTextView.setText(format.format(today));
	 
	 getVefatValues(searchTextView.getText().toString());
	 
	 searchTextView.setOnClickListener(v -> {
		DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view, mYear, mMonth, mDay) -> {
		  
		  calendar.set(mYear, mMonth, mDay);
		  
		  searchTextView.setText(format.format(calendar.getTime()));
		  getVefatValues(searchTextView.getText().toString());
		  
		}, day, month, year);
		datePickerDialog.updateDate(year, month, day);
		datePickerDialog.show();
	 });
	 
	 
  }
  
  private void getVefatValues(String date) {
	 RetroBaseURL2.getmyApi().getVefatValues(CallMethods.getTokenValue(mContext),date).enqueue(new Callback<ModelVefatResponse>() {
		@Override
		public void onResponse(Call<ModelVefatResponse> call, Response<ModelVefatResponse> response) {
		  
		  if (response.body() != null) {
			 
			 List<ModelVefat> modelVefats = response.body().getModelVefats();
			 if (modelVefats.isEmpty()) {
				CallMethods.createSnackBar(mView, "Seçili güne ait herhangi bir veri bulunamadı", 10).show();
			 } else {
				recyclerView.setAdapter(new VefatAdapter(mContext, modelVefats));
			 }
			 progressBar.setVisibility(View.GONE);
			 
		  } else {
			 CallMethods.createSnackBar(mView, getString(R.string.noDataTaken), 2).show();
			 progressBar.setVisibility(View.GONE);
		  }
		  
		}
		
		@Override
		public void onFailure(Call<ModelVefatResponse> call, Throwable t) {
		  CallMethods.createSnackBar(mView, getString(R.string.noDataTaken), 2).show();
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
