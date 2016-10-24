package assignment2.bootcamp.com.nytimessearch.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import assignment2.bootcamp.com.nytimessearch.R;
import assignment2.bootcamp.com.nytimessearch.adapters.ArticleItemsAdapter;
import assignment2.bootcamp.com.nytimessearch.fragments.FilterDialogFragment;
import assignment2.bootcamp.com.nytimessearch.interfaces.NYTSearchApi;
import assignment2.bootcamp.com.nytimessearch.models.ArticleItem;
import assignment2.bootcamp.com.nytimessearch.models.SelectedFilters;
import assignment2.bootcamp.com.nytimessearch.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements Callback<ArticleItem>,
        FilterDialogFragment.FilterSelectedListener{

    @BindView(R.id.rvList)
    RecyclerView rvList;
    private StaggeredGridLayoutManager staggeredLayoutManager;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    private ArticleItemsAdapter articleItemsAdapter;
    private SearchView searchView;

    private String savedQuery;
    private SelectedFilters savedFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(staggeredLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                savedQuery = query;
                refreshData();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void refreshData(){
        if(isNetworkAvailable()) {
            Log.d(Constants.TAG, "Searching for " + savedQuery);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NYTSearchApi nytSearchApi = retrofit.create(NYTSearchApi.class);
            Map<String, String> searchData = new HashMap<>();
            searchData.put("api-key", Constants.API_KEY);
            searchData.put("q", savedQuery);
            //searchData.put(savedFilters.)
            Call<ArticleItem> call = nytSearchApi.getSearchedArticles(searchData);
            call.enqueue(SearchActivity.this);
        }else {
            Toast.makeText(getApplicationContext(), "Please connect to internet first", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
            filterDialogFragment.show(getSupportFragmentManager(), "DatePicker");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<ArticleItem> call, Response<ArticleItem> response) {
        Log.d(Constants.TAG, " Response " + response.code() + " response " + response.message());
        articleItemsAdapter = new ArticleItemsAdapter(this, response.body().getResponse().getDocs());
        rvList.setAdapter(articleItemsAdapter);
    }

    @Override
    public void onFailure(Call<ArticleItem> call, Throwable t) {
        Log.d(Constants.TAG, "Fail " + t.getMessage());
    }

    @Override
    public void onFilterSelectedListener(SelectedFilters selectedFilters) {
        savedFilters = selectedFilters;
        refreshData();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
