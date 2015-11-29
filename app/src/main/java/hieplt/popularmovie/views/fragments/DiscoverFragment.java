package hieplt.popularmovie.views.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
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
import hieplt.popularmovie.commons.Constants;
import hieplt.popularmovie.models.gsons.DiscoverMovieGSON;
import hieplt.popularmovie.models.orms.MovieORMContract;
import hieplt.popularmovie.models.vos.MovieVO;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverBuilder;
import hieplt.popularmovie.services.rests.tmdb.TMDBDiscoverService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by HiepLT on 11/23/15.
 */
@EFragment(R.layout.fragment_discover)
public class DiscoverFragment extends Fragment {

    private final static String LOG_TAG = DiscoverFragment.class.getSimpleName();

    public final static String TAG_NAME = "tablet_fragment_discover";

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

    @AfterViews
    void afterViews() {

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        if (mIsTablet) {

            if (getArguments() != null) {
                if (getArguments().getString(Constants.EXTRA_DISCOVER_MODE) != null) {

                    String discoverMode = getArguments().getString(Constants.EXTRA_DISCOVER_MODE);

                    if (mDiscoverAdapter == null) {
                        mDiscoverAdapter = new DiscoverAdapter(getActivity(), new ArrayList<MovieVO>());
                    }

                    mGvMovies.setAdapter(mDiscoverAdapter);

                    if (discoverMode.equalsIgnoreCase(TMDBDiscoverService.SORT_BY_FAVORITE)) {
                        doLoadFavoriteFromDatabaseInBackground();
                    } else {
                        doLoadDataInBackground(discoverMode);
                    }
                }
            }
        }
    }

    @UiThread
    void doUpdateUI() {
        if (mIsTablet) {
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
        Cursor c = getActivity().getContentResolver().query(MovieORMContract.CONTENT_URI, null,
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

    @ItemClick(R.id.gl_movies)
    void onMovieGridItemClicked(MovieVO selectedMovie) {
        Log.i(LOG_TAG, "onMovieGridItemClicked: selectedMovieVO = " + selectedMovie.getTitle());

        if (mIsTablet) {

            // Build Fragment
            Bundle args = new Bundle();
            args.putParcelable(Constants.EXTRA_DISCOVER_MOVIE, Parcels.wrap(selectedMovie));
            DetailFragment_ detailFragment_ = new DetailFragment_();
            detailFragment_.setArguments(args);

            // Replace
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.tablet_fragment_detail, detailFragment_, DetailFragment_.TAG_NAME).commit();

        }
    }
}
