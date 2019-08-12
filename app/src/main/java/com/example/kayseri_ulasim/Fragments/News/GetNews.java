package com.example.kayseri_ulasim.Fragments.News;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayseri_ulasim.Adapters.NewsAdapter;
import com.example.kayseri_ulasim.Model.ModelOthers.ModelNews;
import com.example.kayseri_ulasim.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//Haberleri çektiğimiz AsyncTask
class GetNews extends AsyncTask<String, Void, String> {
  private WeakReference<Fragment> weakReference;
  private Document document;
  private WeakReference<RecyclerView> recyclerView;
  private WeakReference<ProgressBar> progressBar;
  private WeakReference<TextView> textViewLoading;
  
  
  GetNews(Fragment fragment, RecyclerView recyclerView, ProgressBar progressBar, TextView textViewLoading) {
	 this.weakReference = new WeakReference<>(fragment);
	 this.recyclerView = new WeakReference<>(recyclerView);
	 this.progressBar = new WeakReference<>(progressBar);
	 this.textViewLoading = new WeakReference<>(textViewLoading);
  }
  
  @Override
  protected String doInBackground(String... params) {
	 try {
		//Kayseri haberlerinin bulunduğu sayfaya bağlanır.
		document = Jsoup.connect("https://?????/??").timeout(10000).get();
	 } catch (IOException e) {
		if (weakReference.get() != null)
		 // Log.e("loge", "doInBackground: "+e.getMessage());
		  return "error";
	 }
	 return "right";
  }
  
  @Override
  protected void onPostExecute(String result) {
	 super.onPostExecute(result);
	 
	 if(result.equalsIgnoreCase("error"))
	 {
		Toast.makeText(mContext(), mContext().getString(R.string.noDataTaken), Toast.LENGTH_SHORT).show();
	 }
  
	
	 Fragment fragment = weakReference.get();
	 if (fragment == null || fragment.isRemoving()) return;
	 //Burada widget tanımlayabilmek için weakReference tanımlamak gerekir.
	 
	 if (document != null) {
		List<ModelNews> modelNews = new ArrayList<>();
		Elements mImage_Url = document.select(".item-box-image [src]");       //Resim URL'si
		Elements mTitle = document.select(".item-box-detail [title]");        //Haberin başlık bilgisi
		Elements mTime = document.select(".item-box-detail [datatime]");      //Haberin gün bilgisi
		Elements mContent = document.select(".item-detail-text");             //Haber hakkında kısa bilgi
		Elements mPage_Content = document.select(".item-box-actions [href]"); //Haberin detaylı anlatımının bulunduğu link
	 
		//Tüm haberleri tek tek çek ve recyclerView içerisine ekle. Sonra bunu göster
		//abs:src html kod içerisindeki src ile gösterilen resim linkini çeker.
		//abs:href haber detayının bulunduğu sayfa linkini çeker. Bunu getDetailOfNews sınıfında kullanıyoruz.
		for (int i = 0; i < mTitle.size(); i++) {
		  modelNews.add(new ModelNews(mImage_Url.get(i).attr("abs:src"), mTitle.get(i).text(), mTime.get(i).text(),
					 mContent.get(i).text(), mPage_Content.get(i).attr("abs:href")));
		}
		recyclerView.get().setAdapter(new NewsAdapter(fragment.getContext(), modelNews));
	 }
	 //Haberler yüklenince progressBar'ı kapat.
	 progressBar.get().setVisibility(View.GONE);
	 textViewLoading.get().setVisibility(View.GONE);
  }
  
  private Context mContext() {
	 return weakReference.get().getContext();
  }
  
}
