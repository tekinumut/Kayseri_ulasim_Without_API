package com.example.kayseri_ulasim.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kayseri_ulasim.Fragments.News.GetDetailOfNews;
import com.example.kayseri_ulasim.R;

//Haber detayını gördüğümüz fragment
public class AlertNews extends DialogFragment {
  
  private Context mContext;
  
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
	 View view = View.inflate(mContext, R.layout.alert_news_alert, null);
	 TextView title = view.findViewById(R.id.title_news);
	 ImageView image_url = view.findViewById(R.id.image_news);
	 TextView page_content = view.findViewById(R.id.page_content_news);
	 ProgressBar progressBar = view.findViewById(R.id.progressBar_detailOfNews);
	 
	 //NewsAdapter sınıfından gelen argumentleri burada yakalıyoruz.
	 Bundle bundle = getArguments();
	 
	 if (bundle != null) {
		String mTitle = bundle.getString(mContext.getString(R.string.bundle_news_title));
		String mImage_Url = bundle.getString(mContext.getString(R.string.bundle_news_image_url));
		// String mContent = bundle.getString(mContext.getString(R.string.bundle_news_content));
		String mPage_Content_Url = bundle.getString(mContext.getString(R.string.bundle_news_page_content_url));
		
		title.setText(mTitle);
		
		//Glide ile resim url'sini ImageView'da gösterilecek resime çeviririz.
		Glide.with(mContext)
				  .load(mImage_Url)
				  .into(image_url);
		
		//AsyncTask'ta Haber özetini ve Detay linkini göndererek bu iki bilgiyi birleştiriyor
		//ve bu layoutta gösteriyoruz. Bunu page_content_news TextView'ına yazıyoruz.
		//Parent fragment:NewsFragment
		new GetDetailOfNews(AlertNews.this, page_content, progressBar, mPage_Content_Url).execute();
	 }
	 
	 DialogInterface.OnClickListener listener = ((dialog, which) -> {
		//kapat butonu
		dialog.dismiss();
	 });
	 return new AlertDialog.Builder(mContext)
				.setView(view)
				.setNegativeButton(getString(R.string.close), listener).create();
	 
  }
  
  @Override
  public void onStart() {
	 super.onStart();
	 //Diyaloğun kapat buton rengini, layout başlangıcında oluşturuyoruz.
	 if (getDialog() != null) {
		((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
	/*	int width = ViewGroup.LayoutParams.MATCH_PARENT;
		int height = ViewGroup.LayoutParams.MATCH_PARENT;
		getDialog().getWindow().setLayout(width, height);
	*/
	 }
  }
  
  @Override
  public void onAttach(Context context) {
	 super.onAttach(context);
	 mContext = context;
  }
  
}

