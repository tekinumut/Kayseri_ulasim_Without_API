package com.example.kayseri_ulasim.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;

import com.example.kayseri_ulasim.Abstracts.CallMethods;
import com.example.kayseri_ulasim.MainActivity;
import com.example.kayseri_ulasim.R;

public class AlertChooseDirection extends DialogFragment {

   private Context mContext;
   private String whereYouFrom, name;
   private Double latitude, longitude;
   private String snippet;
   private View mView;

   @NonNull
   @Override
   public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
	  mView = View.inflate(mContext, R.layout.alert_choose_directions, null);

	  Bundle bundle = getArguments();
	  if (bundle != null) {

		 whereYouFrom = bundle.getString(getString(R.string.bundle_whereyoufrom));
		 name = bundle.getString(getString(R.string.bundle_name));
		 snippet = bundle.getString(getString(R.string.bundle_snippet));
		 latitude = bundle.getDouble(getString(R.string.bundle_latitude));
		 longitude = bundle.getDouble(getString(R.string.bundle_longitude));
	  }

	  AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
			  .setView(mView)
			  .setNegativeButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());


	  callButtonClicks(); //Button click işlemlerini en son çağır.

	  return builder.create();

   }

   private void callButtonClicks() {
	  Button myAppMap = mView.findViewById(R.id.dialog_with_my_map);
	  Button myGoogleMap = mView.findViewById(R.id.dialog_with_google_maps);
	  Button myNavigator = mView.findViewById(R.id.dialog_with_navigation);

	  myAppMap.setOnClickListener(v -> {
		 //Uygulama üzerindeki haritaya git
		 FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
		 Bundle bundle = new Bundle();
		 bundle.putString(getString(R.string.bundle_name), name);
		 bundle.putString(getString(R.string.bundle_snippet), snippet);
		 bundle.putDouble(getString(R.string.bundle_latitude), latitude);
		 bundle.putDouble(getString(R.string.bundle_longitude), longitude);

		 AlertShowMyAppMap showMyAppMap = new AlertShowMyAppMap();
		 showMyAppMap.setArguments(bundle);

		 showMyAppMap.show(manager, null);
		 dismiss();
	  });

	  myGoogleMap.setOnClickListener(v -> {
		 //Google Haritalarda uygun durağı aç
		 CallMethods.getDirectionFromMaps(mContext, latitude, longitude, name);
		 dismiss();
	  });
	  myNavigator.setOnClickListener(v -> {
		 //Yol tarifi için navigasyona yönlendir
		 CallMethods.getDirectionFromNavigation(mContext, latitude, longitude);
		 dismiss();
	  });

	  //Eğer Tab2 Map bölümünden geliyorsam haritaya yönlendirme butonunu sil.
	  //Çünkü zaten harita içerisindeyim
	  if (whereYouFrom.equals("AlertMarkerInfoPanel")) {
		 myAppMap.setVisibility(View.GONE);
	  }
   }

   public void onStart() {
	  super.onStart();
	  if (getDialog() != null)
		 ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));

   }

   @Override
   public void onAttach(Context context) {
	  super.onAttach(context);
	  mContext = context;
   }

   @Override
   public void dismiss() {
	  if (getFragmentManager() != null)
		 super.dismiss();
   }

}
