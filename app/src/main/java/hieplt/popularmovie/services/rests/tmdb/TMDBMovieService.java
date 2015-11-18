package hieplt.popularmovie.services.rests.tmdb;

import java.util.Map;

import hieplt.popularmovie.models.gsons.MovieReviewGSON;
import hieplt.popularmovie.models.gsons.MovieTrailerGSON;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by HiepLT on 11/02/15.
 */
public interface TMDBMovieService {

    @GET("/movie/{id}/videos")
    void getTrailers(@Path("id") int movieId, @QueryMap Map<String, String> options, Callback<MovieTrailerGSON> cb);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") int movieId, @QueryMap Map<String, String> options, Callback<MovieReviewGSON> cb);
}
