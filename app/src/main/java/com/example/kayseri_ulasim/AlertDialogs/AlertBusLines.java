package com.example.kayseri_ulasim.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.Abstracts.RestMethods;
import com.example.kayseri_ulasim.Adapters.BusLinesAdapter;
import com.example.kayseri_ulasim.Adapters.LinesOfStopsAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelNewBusLines;
import com.example.kayseri_ulasim.R;

import java.util.ArrayList;
import java.util.List;

public class AlertBusLines extends DialogFragment {
  
  private Context mContext;
  private RecyclerView recyclerView;
  private SearchView searchLines;
  private TextView emptyText;
  private ProgressBar progressBar;
  private View mView;
  private int bus_no;
  private int where_from;
  
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
	 
	 mView = View.inflate(mContext, R.layout.alert_bus_lines, null);
	 
	 
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		// if where =0 gececekotobusler else if =1 gecenhatlar
		bus_no = bundle.getInt(getString(R.string.bundle_bus_no), 10);
		where_from = bundle.getInt(mContext.getString(R.string.bundle_where_busline), 10);
	 }
	 
	 initializeWidgets();
	 
	 //Eğer Geçecek Otobüslere tıkladıysam
	 if (where_from == 0) {
		fetchLines();
		setSearchViewListener();
	 }
	 //Eğer Geçen Hatlara tıkladıysam
	 else if (where_from == 1) {
		fetchLines_Hat();
		setSearchViewListener_Hat();
	 }
	 //Eğer bir şekilde bu diyaloğa hatalı şekilde geldiysem
	 else if (where_from == 10) {
		Toast.makeText(mContext, "Hatalı bir işlem yapıldı.", Toast.LENGTH_LONG).show();
	 }
	 
	 
	 AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setView(mView)
				.setNegativeButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());
	 
	 return builder.create();
  }
  
  private void initializeWidgets() {
	 recyclerView = mView.findViewById(R.id.rec_view_bus_lines);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 
	 if (where_from == 1) {
		//Recyclerview için her bloğu ayırır.
		recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
	 }
	 
	 searchLines = mView.findViewById(R.id.searchLines);
	 progressBar = mView.findViewById(R.id.progressBar_bus_line);
	 emptyText = mView.findViewById(R.id.emptyText);
  }
  
  
  private void fetchLines() {
	 
	 RestMethods.fetchTimesOfLines(bus_no, mContext,(situation, modelBusLines) -> {
		
		if (situation.equals("success")) {
		  if (modelBusLines.size() == 0) {
			 recyclerView.setVisibility(View.GONE);
			 emptyText.setVisibility(View.VISIBLE);
			 progressBar.setVisibility(View.GONE);
		  } else {
			 recyclerView.setAdapter(new BusLinesAdapter(mContext, modelBusLines));
			 progressBar.setVisibility(View.GONE);
			 emptyText.setVisibility(View.GONE);
		  }
		} else if (situation.equals("failure")) {
		  Toast.makeText(mContext, "Veriler alınırken bir hata meydana geldi", Toast.LENGTH_SHORT).show();
		  emptyText.setVisibility(View.VISIBLE);
		  progressBar.setVisibility(View.GONE);
		}
		
	 });
	 
  }
  
  private void fetchLines_Hat() {
	 
	 RestMethods.fetchBusLines(String.valueOf(bus_no), (situation, modelLinesOfStops) -> {
		
		switch (situation) {
		  case "empty":
			 recyclerView.setVisibility(View.GONE);
			 emptyText.setVisibility(View.VISIBLE);
			 progressBar.setVisibility(View.GONE);
			 break;
		  case "success":
			 recyclerView.setAdapter(new LinesOfStopsAdapter(mContext, modelLinesOfStops, getDialog()));
			 progressBar.setVisibility(View.GONE);
			 emptyText.setVisibility(View.GONE);
			 break;
		  case "failure":
			 Toast.makeText(mContext, "Veriler alınırken bir hata meydana geldi", Toast.LENGTH_SHORT).show();
			 emptyText.setVisibility(View.VISIBLE);
			 progressBar.setVisibility(View.GONE);
			 break;
		}
	 });
	 
  }
  
  private void fetchSearchedLines(String line_code, String line_name, boolean warning) {
	 
	 RestMethods.fetchTimesOfLines(bus_no, mContext,(situation, busLines) -> {
		
		if (situation.equals("success")) {
		  List<ModelNewBusLines> modelBusLines = new ArrayList<>();
		  
		  for (int i = 0; i < busLines.size(); i++) {
			 
			 //Eğer searchView'de aranan içerik veritabanında var ise onu ekle
			 if (CallMethods.toEnglish(busLines.get(i).getHatAdi()).toUpperCase().contains(CallMethods.toEnglish(line_name).toUpperCase()) ||
						String.valueOf(busLines.get(i).getHatKod()).contains(line_code)) {
				modelBusLines.add(busLines.get(i));
			 }
			 
			 //Arama bittiğinde  adapter'ı aranan içeriğe ayarla.
			 if (i == busLines.size() - 1) {
				recyclerView.setAdapter(null);
				recyclerView.setAdapter(new BusLinesAdapter(mContext, modelBusLines));
			 }
			 if (warning && modelBusLines.isEmpty()) {
				Toast.makeText(mContext, "Aramalarınıza uygun bir kayıt bulunamadı", Toast.LENGTH_SHORT).show();
			 }
			 
		  }
		} else if (situation.equals("failure")) {
		  Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
		}
		
	 });
	 
	 
  }
  
  private void fetchSearchedLines_Hat(String text, boolean warning) {
	 
	 RestMethods.fetchSearchedLinesOfStops(text, (situation, modelLinesOfStops) -> {
		
		switch (situation) {
		  case "empty":
			 if (warning)
				Toast.makeText(mContext, getString(R.string.noDataFound), Toast.LENGTH_SHORT).show();
			 break;
		  case "success":
			 recyclerView.setAdapter(null);
			 recyclerView.setAdapter(new LinesOfStopsAdapter(mContext, modelLinesOfStops, getDialog()));
			 break;
		  case "failure":
			 Toast.makeText(mContext, getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
			 break;
		}
	 });
	 
  }
  
  private void setSearchViewListener() {
	 
	 searchLines.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String text) {
		  
		  //Text boş olursa tüm hatları tekrar listele
		  if (text.isEmpty()) {
			 fetchLines();
			 searchLines.clearFocus();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedLines(text, text, true);
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
			 //Devamlı kontrol yapılacağı için her defasında Toast mesajı çıkmaması için
			 fetchSearchedLines(newText, newText, false);
		  }
		  
		  return true;
		}
	 });
  }
  
  private void setSearchViewListener_Hat() {
	 
	 searchLines.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String text) {
		  
		  //Text boş olursa tüm hatları tekrar listele
		  if (text.isEmpty()) {
			 fetchLines_Hat();
			 searchLines.clearFocus();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedLines_Hat(text, true);
			 searchLines.clearFocus();
		  }
		  
		  return true;
		}
		
		@Override
		public boolean onQueryTextChange(String newText) {
		  //Text boş olursa tüm hatları tekrar listele
		  if (newText.isEmpty()) {
			 fetchLines_Hat();
		  } else {
			 //Eğer içerikte bir kelime varsa ona göre arama işlemini gerçekleştir :)
			 fetchSearchedLines_Hat(newText, false);
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
