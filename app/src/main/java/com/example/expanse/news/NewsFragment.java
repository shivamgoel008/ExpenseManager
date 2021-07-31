package com.example.expanse.news;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expanse.BuildConfig;
import com.example.expanse.R;
import com.example.expanse.news.api.ApiClient;
import com.example.expanse.news.api.ApiInterface;
import com.example.expanse.news.models.Article;
import com.example.expanse.news.models.News;
import com.example.expanse.ui.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public NewsFragment() {
    }

    public static final String API_KEY= BuildConfig.MY_API_KEY;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles=new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout= view.findViewById(R.id.news_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);

        recyclerView = view.findViewById(R.id.news_recyclerView);
        layoutManager=new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");
    }

    public  void LoadJson(final String keyword){

        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utils.getCountry();
        String language=Utils.getLanguage();
        Call<News> call ;

        if(keyword.length()>0){
            call= apiInterface.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        }
        else {
            call = apiInterface.getNews(country, API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticle()!= null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }

                    articles=response.body().getArticle();
                    adapter= new Adapter(articles,requireActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(requireActivity(), "No Result", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {

        inflater= requireActivity().getMenuInflater();
        inflater.inflate(R.menu.news_menu,menu);
        SearchManager searchManager=(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView=(SearchView)menu.findItem(R.id.news_search).getActionView();
        MenuItem searchMenuItem=menu.findItem(R.id.news_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>2){
                    onLoadingSwipeRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                LoadJson(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }





    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }


    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
//
//                Article article = articles.get(position);
//                intent.putExtra("url",article.getUrl());
//                intent.putExtra("title", article.getTitle());
//                intent.putExtra("img",article.getUrlToImage());
//                intent.putExtra("date",article.getPublishedAt());
//                intent.putExtra("source",article.getSource().getName());
//                intent.putExtra("author",article.getAuthor());
//
//                startActivity(intent);
            }
        });
    }




//    @Override
//    public void onRefresh() {
//
//    }
}