package com.example.kayseri_ulasim.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Adapters.HtgAlternativesAdapter;
import com.example.kayseri_ulasim.Model.ModelHowToGoAll;
import com.example.kayseri_ulasim.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HtgAlternativesFragment extends Fragment {
  
  private Context mContext;
  private View mView;
  private RecyclerView recyclerView;
  private ArrayList<String> arrayList = new ArrayList<>();
  private ArrayList<ModelHowToGoAll> newList = new ArrayList<>();
  private HashMap<String, ModelHowToGoAll> hashMapChilds = new HashMap<>();
  private String underStand;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 mView = inflater.inflate(R.layout.fragment_htg_alternatives, container, false);
	 
	 initializeWidgets();
	 
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		Type type = new TypeToken<List<ModelHowToGoAll>>() {
		}.getType();
		String jsonValue = bundle.getString("jsonHowToGoAll", "");
		List<ModelHowToGoAll> modelHowToGoAlls = new Gson().fromJson(jsonValue, type);
		
		Collections.sort(modelHowToGoAlls, (c1, c2) -> Double.compare(c1.getToplamYurumeMesafesi(), c2.getToplamYurumeMesafesi()));
		
		
		//Aynı başlangıç ve bitiş duraklarına sahip verileri burada yok ediyoruz.
		
		//Gelen tüm yol tarif verilerini al
		for (int i = 0; i < modelHowToGoAlls.size(); i++) {
		  
		  //başlangıç ve bitiş duraklarını bir String'te birleştir.
		  String underStand = createSecretKey(modelHowToGoAlls.get(i).getHtgList().getBeginStop(), modelHowToGoAlls.get(i).getHtgList().getEndStop());
		  //Bu birleşen değerleri arrayList'e at.
		  arrayList.add(underStand);
		  
		  //eğer aynı başlangıç ve bitiş durakları tekrar etmediyse veriyi ekle
		  if (Collections.frequency(arrayList, underStand) < 2) {
			 //Burada tekrar etmeyen durakları newList'e atıyoruz.
			 //Bu new list recyclerview'da gösterilecek.
			 // Daha sonra bu listenin her elemanına aynı underStand verisine sahip childList(tekrar eden) verileri ekleyeceğiz.
			 if (modelHowToGoAlls.get(i).getIlkDuragaUzaklik() < 1000 && modelHowToGoAlls.get(i).getSonDuragaUzaklik() < 1500) {
				newList.add(modelHowToGoAlls.get(i));
			 }
			 
		  } else {
			 //Tekrar eden veriler burada işleme alınacak.
			 //bu verileri tek tek kontrol edebilmek için hasmap'e kayıt et.
			 hashMapChilds.put(arrayList.get(i), modelHowToGoAlls.get(i));
		  }
		  
		  if (i == modelHowToGoAlls.size() - 1) {
			 //newListin underStand kodu ile hashmap'ten veri çekeceğiz.
			 //Bu kod ile recyclerList'e eklenen her ana objeye çocuklarını da göndereceğiz :)
			 
			 if (newList.size() == 0) {
				Toast.makeText(mContext, "Güzergah Bulunamadı", Toast.LENGTH_SHORT).show();
			 } else {
				recyclerView.setAdapter(new HtgAlternativesAdapter(mContext, newList, hashMapChilds));
			 }
			 
			 
		  }
		}
		
		
	 }
	 
	 
	 return mView;
  }
  
  private String createSecretKey(int beginStop, int endStop) {
	 return "" + beginStop + "" + endStop + "" + (beginStop + endStop);
	 
  }
  
  
  private void initializeWidgets() {
	 recyclerView = mView.findViewById(R.id.recycler_htg_alternavies);
	 recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
	 recyclerView.setHasFixedSize(true);
	 //Recyclerview için her bloğu ayırır.
	 
	 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
				LinearLayoutManager.VERTICAL);
	 recyclerView.addItemDecoration(dividerItemDecoration);
  }
  
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
  
}
