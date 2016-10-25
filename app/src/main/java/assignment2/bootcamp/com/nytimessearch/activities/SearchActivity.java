package assignment2.bootcamp.com.nytimessearch.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment2.bootcamp.com.nytimessearch.R;
import assignment2.bootcamp.com.nytimessearch.adapters.ArticleItemsAdapter;
import assignment2.bootcamp.com.nytimessearch.fragments.FilterDialogFragment;
import assignment2.bootcamp.com.nytimessearch.interfaces.NYTSearchApi;
import assignment2.bootcamp.com.nytimessearch.models.ArticleItem;
import assignment2.bootcamp.com.nytimessearch.models.Doc;
import assignment2.bootcamp.com.nytimessearch.models.SelectedFilters;
import assignment2.bootcamp.com.nytimessearch.utils.Constants;
import assignment2.bootcamp.com.nytimessearch.utils.EndlessRecyclerViewScrollListener;
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

    private ArticleItemsAdapter articleItemsAdapter;
    private SearchView searchView;

    private String savedQuery;
    private SelectedFilters savedFilters;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<Doc> articleItems = new ArrayList<>();
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        staggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(staggeredLayoutManager);

        articleItemsAdapter = new ArticleItemsAdapter(this, articleItems);
        rvList.setAdapter(articleItemsAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(staggeredLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                loadMoreData(page);
            }
        };
        rvList.addOnScrollListener(scrollListener);
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
                articleItems.clear();
                scrollListener.resetState();

                refreshData(0);
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

    /**
     * Get date from NYTimes for selected page with appropriate filters applied
     * @param page
     */
    public void refreshData(final int page){
        Log.d(Constants.TAG, "page number " + page);
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
            searchData.put("page", Integer.toString(page));
            if(savedFilters != null) {
                if(!savedFilters.getDate().equals("")) {
                    searchData.put("begin_date", savedFilters.getDate());
                }
                if(!savedFilters.getSortOrder().equals("")) {
                    searchData.put("sort", savedFilters.getSortOrder());
                }

                if(savedFilters.isArts() || savedFilters.isFashion() || savedFilters.isSports()) {
                    StringBuilder builder = new StringBuilder("news_desk:(");
                    if (savedFilters.isArts()) {
                        builder.append("\"").append(getString(R.string.arts)).append("\"").append(",");
                    }
                    if (savedFilters.isFashion()) {
                        builder.append("\"").append(getString(R.string.fashion_amp_style)).append("\"").append(",");
                    }
                    if (savedFilters.isSports()) {
                        builder.append("\"").append(getString(R.string.sports)).append("\"").append(",");
                    }

                    builder.append(")");
                    String newsDeskString = builder.toString().replace(" ","");
                    searchData.put("fq", newsDeskString);
                    Log.d(Constants.TAG, "Desk Filter is " + newsDeskString);
                }

            }
            Call<ArticleItem> call = nytSearchApi.getSearchedArticles(searchData);
            call.enqueue(SearchActivity.this);
        }else {
            Toast.makeText(getApplicationContext(), R.string.msg_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void loadMoreData(int page) {
        currentPage = page;
        refreshData(page);
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

        if(response.code() == 200 /*OK*/) {
            List<Doc> resultToShow = response.body().getResponse().getDocs();
            Log.d(Constants.TAG, "Response size " + resultToShow.size());
            articleItems.addAll(resultToShow);
            articleItemsAdapter.notifyDataSetChanged();
            if(resultToShow.size() == 0  && currentPage == 0) {
                Toast.makeText(getApplicationContext(), R.string.msg_no_results, Toast.LENGTH_LONG).show();
            }
        } else if( currentPage == 0) {
            Toast.makeText(getApplicationContext(), R.string.msg_no_results, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ArticleItem> call, Throwable t) {
        Log.d(Constants.TAG, "Fail " + t.getMessage());
        refreshData(currentPage);
    }

    @Override
    public void onFilterSelectedListener(SelectedFilters selectedFilters) {
        savedFilters = selectedFilters;
        scrollListener.resetState();
        articleItems.clear();
        refreshData(0);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
