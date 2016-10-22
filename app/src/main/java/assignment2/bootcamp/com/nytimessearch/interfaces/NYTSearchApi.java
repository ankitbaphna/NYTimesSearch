package assignment2.bootcamp.com.nytimessearch.interfaces;

import java.util.Map;

import assignment2.bootcamp.com.nytimessearch.models.ArticleItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by baphna on 10/21/2016.
 */

public interface NYTSearchApi {

    @GET("/svc/search/v2/articlesearch.json")
    Call<ArticleItem> getSearchedArticles(@QueryMap Map<String, String> searchOptions);

}
