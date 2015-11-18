package hieplt.popularmovie.activities;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import hieplt.popularmovie.R;
import hieplt.popularmovie.adapters.DiscoverAdapter;
import hieplt.popularmovie.bases.PopMovieActivityBase;
import hieplt.popularmovie.commons.Constants;
import hieplt.popularmovie.models.gsons.DiscoverMovieGSON;
import hieplt.popularmovie.models.vos.MovieVO;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverBuilder;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@Fullscreen
@EActivity(R.layout.activity_discover)
public class DiscoverActivity extends PopMovieActivityBase {

    private final static String LOG_TAG = DiscoverActivity.class.getSimpleName();

    // Services
    @Bean
    TMDBDiscoverBuilder mTMDBDiscoverBuilder;

    // Views
    @ViewById(R.id.gl_movies)
    GridView mGvMovies;

    @ViewById(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    // Adapters
    DiscoverAdapter mDiscoverAdapter;

    // Data Value Object
    List<MovieVO> mMovieVOs;

    @AfterViews
    void initViews() {

        onSetupSupportActionBar(mToolbar);

        if (mDiscoverAdapter == null) {
            mDiscoverAdapter = new DiscoverAdapter(this, new ArrayList<MovieVO>());
        }

        mGvMovies.setAdapter(mDiscoverAdapter);

        doLoadDataInBackground(TMDBDiscoverService.SORT_BY_POPULAR);
    }

    @UiThread
    void doUpdateUI() {
        mDiscoverAdapter.clear();
        mDiscoverAdapter.addAll(mMovieVOs);
        mDiscoverAdapter.notifyDataSetChanged();
    }

    @Background(serial = "load-tmdb-discover-movies")
    void doLoadDataInBackground(String mSortBy) {

        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put("api_key", Constants.TMDB_API_KEY);
        mParams.put("sort_by", mSortBy);

        mTMDBDiscoverBuilder.getService().getMovies(mParams, new Callback<DiscoverMovieGSON>() {
            @Override
            public void success(DiscoverMovieGSON gson, Response response) {

                // Convert to value object
                mMovieVOs = new ArrayList<>();

                List<DiscoverMovieGSON.DiscoverResult> mResults = gson.mResults;

                Iterator<DiscoverMovieGSON.DiscoverResult> iterator = mResults.iterator();
                MovieVO movieVO;
                while (iterator.hasNext()) {
                    DiscoverMovieGSON.DiscoverResult movieRawVO = iterator.next();
                    movieVO = new MovieVO();

                    // Construct info into object.
                    movieVO.setTitle(movieRawVO.mTitle);
                    movieVO.setReleaseDate(movieRawVO.mReleaseDate);
                    movieVO.setThumbnailsURL(new StringBuilder()
                            .append(Constants.TMDB_IMAGE_BASE_URL)
                            .append(MovieVO.ThumbnailSize.mW342)
                            .append(movieRawVO.mPosterPath)
                            .toString());
                    movieVO.setVoteAverage(String.valueOf(movieRawVO.mVoteAverage));
                    movieVO.setPlotSynopsis(movieRawVO.mOverview);
                    movieVO.setId(movieRawVO.mId);
                    // I'm thinking about how to cache poster as drawable.

                    mMovieVOs.add(movieVO);
                }

                Log.i(LOG_TAG, gson.toString());
                Log.i(LOG_TAG, response.toString());

                doUpdateUI();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(LOG_TAG, error.toString());
            }
        });
    }

    @ItemClick(R.id.gl_movies)
    void onMovieGridItemClicked(MovieVO selectedMovie) {
        Log.i(LOG_TAG, "onMovieGridItemClicked: selectedMovieVO = " + selectedMovie.getTitle());

        Intent intent = new Intent(this, DetailActivity_.class);
        intent.putExtra(Constants.EXTRA_DISCOVER_MOVIE, Parcels.wrap(selectedMovie));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            doLoadDataInBackground(TMDBDiscoverService.SORT_BY_POPULAR);
        } else if (id == R.id.action_high_rate) {
            doLoadDataInBackground(TMDBDiscoverService.SORT_BY_HIGHEST_RATE);
        }

        return super.onOptionsItemSelected(item);
    }
}
