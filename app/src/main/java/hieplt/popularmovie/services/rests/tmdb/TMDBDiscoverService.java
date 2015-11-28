package hieplt.popularmovie.services.rests.tmdb;

import java.util.Map;

import hieplt.popularmovie.models.gsons.DiscoverMovieGSON;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import retrofit.Callback;

/**
 * Created by HiepLT on 11/02/15.
 */
public interface TMDBDiscoverService {

    // Sort By
    public final String SORT_BY_POPULAR         = "popularity.desc";
    public final String SORT_BY_HIGHEST_RATE    = "vote_average.desc";
    public final String SORT_BY_FAVORITE        = "favorite";

    @GET("/discover/movie")
    void getMovies(@QueryMap Map<String, String> options, Callback<DiscoverMovieGSON> cb);
}
