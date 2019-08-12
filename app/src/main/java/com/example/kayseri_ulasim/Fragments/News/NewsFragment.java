package com.example.kayseri_ulasim.Fragments.News;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kayseri_ulasim.R;

//Ana sayfa fragment'ı
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewLoading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);

        setRecyclerView();

        progressBar=rootView.findViewById(R.id.progressBar_news);
        textViewLoading=rootView.findViewById(R.id.textView_loading_news);

        callNews();

        return rootView;

    }

    private void setRecyclerView() {
        recyclerView = rootView.findViewById(R.id.rec_view_news);
        refreshLayout = rootView.findViewById(R.id.swipe_refresh_news);
        refreshLayout.setOnRefreshListener(NewsFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        //Recyclerview için her bloğu ayırır.
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onRefresh() {
        //Tüm haberleri tekrar yükle
        refreshLayout.postDelayed(this::callNews, 500);
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void callNews() {
        //Haberleri RecyclerView içerisine çeker.
        //WeakReference olarak bu sayfayı gönderiyoruz. ProgressBar gösterimini AsyncTask içerisinde gerçekleştiriyoruz.
        new GetNews(NewsFragment.this,recyclerView,progressBar,textViewLoading).execute();
    }
}
