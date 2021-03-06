package hieplt.popularmovie.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
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
import hieplt.popularmovie.models.orms.MovieORMContract;
import hieplt.popularmovie.models.vos.MovieVO;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverBuilder;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverService;
import hieplt.popularmovie.views.fragments.DetailFragment_;
import hieplt.popularmovie.views.fragments.DiscoverFragment_;
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

    // Mode Detection
    boolean mIsTablet = false;

    @InstanceState
    String mLastSortBy;

    private static final int ACTIVITY_REQUEST_CODE = 0;

    @AfterViews
    void afterViews() {

        onSetupSupportActionBar(mToolbar);

        mLastSortBy = TMDBDiscoverService.SORT_BY_POPULAR;

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        if (!mIsTablet) {

            if (mDiscoverAdapter == null) {
                mDiscoverAdapter = new DiscoverAdapter(this, new ArrayList<MovieVO>());
            }

            mGvMovies.setAdapter(mDiscoverAdapter);

            doLoadDataInBackground(mLastSortBy);

        } else {

            // Build Fragment
            Bundle args = new Bundle();
            args.putString(Constants.EXTRA_DISCOVER_MODE, mLastSortBy);
            DiscoverFragment_ discoverFragment_ = new DiscoverFragment_();
            discoverFragment_.setArguments(args);

            // Replace
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.tablet_fragment_discover, discoverFragment_, DiscoverFragment_.TAG_NAME).commit();
        }
    }

    @UiThread
    void doUpdateUI() {
        if (!mIsTablet) {
            mDiscoverAdapter.clear();
            mDiscoverAdapter.addAll(mMovieVOs);
            mDiscoverAdapter.notifyDataSetChanged();
        }
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

    @Background(serial = "load-db-favorite-movies")
    void doLoadFavoriteFromDatabaseInBackground() {

        // TODO - Implement Favorite List.
        Cursor c = getContentResolver().query(MovieORMContract.CONTENT_URI, null,
                null, null, null);

        // Build value object
        mMovieVOs = new ArrayList<>();
        MovieVO movieVO;
        while(c.moveToNext()) {
            movieVO = new MovieVO();

            // Construct info into object.
            for (int i = 0; i < c.getColumnCount(); i++) {
                // Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));

                String columnName = c.getColumnName(i);

                if (columnName.equalsIgnoreCase(MovieORMContract.TITLE)) {
                    movieVO.setTitle(c.getString(i));
                }

                if (columnName.equalsIgnoreCase(MovieORMContract.RELEASEDATE)) {
                    movieVO.setReleaseDate(c.getString(i));
                }

                if (columnName.equalsIgnoreCase(MovieORMContract.THUMBNAILSURL)) {
                    movieVO.setThumbnailsURL(new StringBuilder()
                            .append(Constants.TMDB_IMAGE_BASE_URL)
                            .append(MovieVO.ThumbnailSize.mW342)
                            .append(c.getString(i))
                            .toString());
                }

                if (columnName.equalsIgnoreCase(MovieORMContract.VOTEAVERAGE)) {
                    movieVO.setVoteAverage(c.getString(i));
                }

                if (columnName.equalsIgnoreCase(MovieORMContract.PLOTSYNOPSIS)) {
                    movieVO.setPlotSynopsis(c.getString(i));
                }

                if (columnName.equalsIgnoreCase(MovieORMContract._ID)) {
                    movieVO.setId(c.getInt(i));
                }
            }

            mMovieVOs.add(movieVO);
        }
        c.close();

        doUpdateUI();
    }

    /**
     * This method is always works for Smart Phone, non-fragment mechanism.
     * @param selectedMovie
     */
    @ItemClick(R.id.gl_movies)
    void onMovieGridItemClicked(MovieVO selectedMovie) {
        Log.i(LOG_TAG, "onMovieGridItemClicked: selectedMovieVO = " + selectedMovie.getTitle());

        if (mIsTablet) {
            // Check logic on fragment
        } else {
            Intent intent = new Intent(this, DetailActivity_.class);
            intent.putExtra(Constants.EXTRA_DISCOVER_MOVIE, Parcels.wrap(selectedMovie));
            startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
        }
    }

    @OnActivityResult(ACTIVITY_REQUEST_CODE)
    void onResult(int resultCode) {
        if (!mIsTablet) {
            if (mLastSortBy.equalsIgnoreCase(TMDBDiscoverService.SORT_BY_FAVORITE)) {
                doLoadFavoriteFromDatabaseInBackground();
            } else {
                doLoadDataInBackground(mLastSortBy);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (!mIsTablet) {
            if (id == R.id.action_popular) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_POPULAR;
                doLoadDataInBackground(mLastSortBy);
            } else if (id == R.id.action_high_rate) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_HIGHEST_RATE;
                doLoadDataInBackground(mLastSortBy);
            } else if (id == R.id.action_favorite) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_FAVORITE;
                doLoadFavoriteFromDatabaseInBackground();
            }

        } else {

            // FRAGMENT
            // Build Fragment
            Bundle args = new Bundle();

            if (id == R.id.action_popular) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_POPULAR;
                args.putString(Constants.EXTRA_DISCOVER_MODE, mLastSortBy);
            } else if (id == R.id.action_high_rate) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_HIGHEST_RATE;
                args.putString(Constants.EXTRA_DISCOVER_MODE, mLastSortBy);
            } else if (id == R.id.action_favorite) {
                mLastSortBy = TMDBDiscoverService.SORT_BY_FAVORITE;
                args.putString(Constants.EXTRA_DISCOVER_MODE, mLastSortBy);
            }

            DiscoverFragment_ discoverFragment_ = new DiscoverFragment_();
            discoverFragment_.setArguments(args);

            // Replace
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.tablet_fragment_discover, discoverFragment_, DiscoverFragment_.TAG_NAME);

            // DETAIL FRAGMENT
            // Replace with blank fragment
            fragmentTransaction.replace(R.id.tablet_fragment_detail, new Fragment(), DetailFragment_.TAG_NAME).commit();
        }

        return super.onOptionsItemSelected(item);
    }
}