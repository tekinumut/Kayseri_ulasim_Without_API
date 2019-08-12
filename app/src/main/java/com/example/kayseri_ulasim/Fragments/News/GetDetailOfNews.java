package com.example.kayseri_ulasim.Fragments.News;

import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kayseri_ulasim.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;

//Haber Detayının AlertNews sınıfındaki layouta yazıldığı sınıftır.
public class GetDetailOfNews extends AsyncTask<Void, Void, Void> {

    private Document document;
    private String URL;
    private WeakReference<Fragment> weakReference;
    private WeakReference<TextView> page_content;
    private WeakReference<ProgressBar> progressBar;

    public GetDetailOfNews(Fragment fragment, TextView page_content, ProgressBar progressBar, String URL) {
        this.weakReference = new WeakReference<>(fragment);
        this.progressBar = new WeakReference<>(progressBar);
        this.page_content = new WeakReference<>(page_content);
        this.URL = URL;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            document = Jsoup.connect(URL).timeout(10000).get();
        } catch (IOException e) {
            //internete bağlanamazsa
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void values) {
        super.onPostExecute(values);
        Fragment fragment = weakReference.get();
        if (fragment == null) return;

        page_content.get().setText(mContext().getString(R.string.noDataTaken));

        if (document != null) {
            //İlgili sayfadaki haber detayını Elementse aktar.
            Elements mPage_Content = document.select(".page-content");
            StringBuilder mBuilder = new StringBuilder();
            //page_content TextView'ına aldığı haberi özet+detay şeklinde yazar.
            for (int i = 0; i < mPage_Content.size(); i++) {
                page_content.get().setText(mBuilder.append(mPage_Content.get(i).text()));
            }
        }
        progressBar.get().setVisibility(View.GONE);
    }

    private Context mContext() {
        return weakReference.get().getContext();
    }
}
