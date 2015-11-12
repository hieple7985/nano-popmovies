package hieplt.popularmovie.services.rests;

import java.util.Map;

import hieplt.popularmovie.models.gsons.DiscoverMovieGSON;
import retrofit.http.GET;
import retrofit.http.QueryMap;
import retrofit.Callback;

/**
 * Created by HiepLT on 11/02/15.
 */
public interface TMDBDiscoverService {

    @GET("/discover/movie")
    void getMovies(@QueryMap Map<String, String> options, Callback<DiscoverMovieGSON> cb);

//    @Query("address") String address,
    // @GET("/discover/tv")
}
