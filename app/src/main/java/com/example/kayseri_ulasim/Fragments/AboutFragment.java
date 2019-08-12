package com.example.kayseri_ulasim.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kayseri_ulasim.R;
import com.droidbyme.dialoglib.DroidDialog;

public class AboutFragment extends Fragment {
  
  private Context mContext;
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	 
	 View rootView = inflater.inflate(R.layout.fragment_about, container, false);
	 
	 TextView privacy = rootView.findViewById(R.id.privacy_pol);
	 TextView githubKodlari = rootView.findViewById(R.id.gitHubKodlari);
	 
	 privacy.setOnClickListener(v -> new DroidDialog.Builder(mContext)
				.icon(R.drawable.ic_info)
				.title("Uygulama Tanıtımı")
				.content("Hiçbir verinizi kullanmıyor, depolamıyor veya herhangi bir şekilde verileriniz ile etkileşimde bulunmuyoruz. \n" +
						  "Uygulama açık kaynaklıdır. Uygulamanın kaynak kodlarına aşağıdaki bölümden erişebilirsiniz.")
				.cancelable(false, false)
				.positiveButton("Linke git", dialog -> {
				  startActivity(new Intent(Intent.ACTION_VIEW,
							 Uri.parse("???.php")));
				  dialog.dismiss();
				  
				})
				.negativeButton("Çıkış", Dialog::dismiss)
				.show());
	 
	 githubKodlari.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://github.com/tekinumut/Kayseri_ulasim_Without_API"))));
	 
	 
	 return rootView;
  }
  
  @Override
  public void onAttach(@NonNull Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
}
