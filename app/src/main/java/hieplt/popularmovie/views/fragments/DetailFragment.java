package hieplt.popularmovie.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
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
import hieplt.popularmovie.adapters.ReviewAdapter;
import hieplt.popularmovie.adapters.TrailerAdapter;
import hieplt.popularmovie.commons.Constants;
import hieplt.popularmovie.models.gsons.MovieReviewGSON;
import hieplt.popularmovie.models.gsons.MovieTrailerGSON;
import hieplt.popularmovie.models.orms.MovieORMContract;
import hieplt.popularmovie.models.vos.MovieVO;
import hieplt.popularmovie.models.vos.ReviewVO;
import hieplt.popularmovie.models.vos.TrailerVO;
import hieplt.popularmovie.services.rests.tmdb.TMDBMovieBuilder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by HiepLT on 11/23/15.
 */
@EFragment(R.layout.fragment_detail)
public class DetailFragment extends Fragment {

    private final static String LOG_TAG = DetailFragment.class.getSimpleName();

    public final static String TAG_NAME = "tablet_fragment_detail";

    // Context
    private Context mContext;

    // Services
    @Bean
    TMDBMovieBuilder mTMDBMovieBuilder;

    // Views - Common
    @ViewById(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    // Views - Detail
    @ViewById(R.id.item_movie_title)
    TextView mTvTitle;

    @ViewById(R.id.item_movie_release_date_value)
    TextView mTvReleaseDate;

    @ViewById(R.id.item_movie_poster)
    ImageView mIvPoster;

    @ViewById(R.id.item_movie_vote_average_value)
    TextView mTvVoteAverage;

    @ViewById(R.id.item_movie_vote_favorite_checkbox)
    CheckBox mCbFavorite;

    @ViewById(R.id.item_movie_plot_synopsis)
    TextView mTvPlotSynopsis;

    @ViewById(R.id.lv_movie_trailers)
    ListView mlvTrailers;

    @ViewById(R.id.lv_movie_reviews)
    ListView mlvReviews;

    // Adapters
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;

    // Data - Value Object
    List<TrailerVO> mTrailerVOs;
    List<ReviewVO> mReviewVOs;

    // Data - Model
    MovieVO mMovieVO;

    // Content Provider
    // TMDBProvider mTMDBProvider;

    @AfterViews
    void initViews() {

        mContext = getActivity().getApplicationContext();

        if (getArguments() != null) {
            if (getArguments().getParcelable(Constants.EXTRA_DISCOVER_MOVIE) != null) {
                mMovieVO = Parcels.unwrap(getArguments().getParcelable(Constants.EXTRA_DISCOVER_MOVIE));

                if (mMovieVO != null) {

                    Cursor c = getActivity().getContentResolver().query(MovieORMContract.CONTENT_URI, null,
                            "_ID = ?", new String[]{String.valueOf(mMovieVO.getId())}, null);

                    if (c.getCount() <= 0) {
                        mCbFavorite.setChecked(false);
                    } else {
                        mCbFavorite.setChecked(true);
                    }

                    mTvTitle.setText(mMovieVO.getTitle());
                    Picasso.with(mContext).load(mMovieVO.getThumbnailsURL()).into(mIvPoster);
                    mTvReleaseDate.setText(mMovieVO.getReleaseDate());
                    mTvVoteAverage.setText(mMovieVO.getVoteAverage());
                    mTvPlotSynopsis.setText(mMovieVO.getPlotSynopsis());

                    if (mTrailerAdapter == null) {
                        mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<TrailerVO>());
                    }
                    mlvTrailers.setAdapter(mTrailerAdapter);

                    if (mReviewAdapter == null) {
                        mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<ReviewVO>());
                    }
                    mlvReviews.setAdapter(mReviewAdapter);

                    // Load more detailed data.
                    doLoadTrailersInBackground(mMovieVO.getId());
                    doLoadReviewsInBackground(mMovieVO.getId());
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // In Background Processing
    // ------------------------------------------------------------------------
    @Background(serial = "load-tmdb-movies-trailers")
    void doLoadTrailersInBackground(int movieId) {
        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put("api_key", Constants.TMDB_API_KEY);

        mTMDBMovieBuilder.getService().getTrailers(movieId, mParams, new Callback<MovieTrailerGSON>() {
            @Override
            public void success(MovieTrailerGSON gson, Response response) {

                // Convert to value object
                mTrailerVOs = new ArrayList<>();

                List<MovieTrailerGSON.TrailerResult> mResults = gson.mResults;

                Iterator<MovieTrailerGSON.TrailerResult> iterator = mResults.iterator();
                TrailerVO trailerVO;
                while (iterator.hasNext()) {
                    MovieTrailerGSON.TrailerResult trailerRawVO = iterator.next();
                    trailerVO = new TrailerVO();

                    // Construct info into object.
                    trailerVO.setName(trailerRawVO.mName);
                    trailerVO.setSize(trailerRawVO.mSize);
                    trailerVO.setSite(trailerRawVO.mSite);
                    trailerVO.setKey(trailerRawVO.mKey);

                    mTrailerVOs.add(trailerVO);
                }

                Log.i(LOG_TAG, gson.toString());
                Log.i(LOG_TAG, response.toString());

                doTrailersUpdateUI();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(LOG_TAG, error.toString());
            }
        });
    }

    @Background(serial = "load-tmdb-movies-reviews")
    void doLoadReviewsInBackground(int movieId) {
        HashMap<String, String> mParams = new HashMap<String, String>();
        mParams.put("api_key", Constants.TMDB_API_KEY);

        mTMDBMovieBuilder.getService().getReviews(movieId, mParams, new Callback<MovieReviewGSON>() {
            @Override
            public void success(MovieReviewGSON gson, Response response) {

                // Convert to value object
                mReviewVOs = new ArrayList<>();

                List<MovieReviewGSON.ReviewResult> mResults = gson.mResults;

                Iterator<MovieReviewGSON.ReviewResult> iterator = mResults.iterator();
                ReviewVO reviewVO;
                while (iterator.hasNext()) {
                    MovieReviewGSON.ReviewResult reviewRawVO = iterator.next();
                    reviewVO = new ReviewVO();

                    // Construct info into object.
                    reviewVO.setAuthor(reviewRawVO.mAuthor);
                    reviewVO.setContent(reviewRawVO.mContent);

                    mReviewVOs.add(reviewVO);
                }

                Log.i(LOG_TAG, gson.toString());
                Log.i(LOG_TAG, response.toString());

                doReviewsUpdateUI();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(LOG_TAG, error.toString());
            }
        });
    }

    // ------------------------------------------------------------------------
    // UI Updating Processing
    // ------------------------------------------------------------------------
    @UiThread
    void doTrailersUpdateUI() {
        mTrailerAdapter.clear();
        mTrailerAdapter.addAll(mTrailerVOs);
        mTrailerAdapter.getCount();
        mTrailerAdapter.notifyDataSetChanged();
    }

    @UiThread
    void doReviewsUpdateUI() {
        mReviewAdapter.clear();
        mReviewAdapter.addAll(mReviewVOs);
        mReviewAdapter.notifyDataSetChanged();
    }

    // ------------------------------------------------------------------------
    // Events Binding
    // ------------------------------------------------------------------------
    @ItemClick(R.id.lv_movie_trailers)
    void onMovieTrailerItemClicked(TrailerVO selectedTrailerVO) {

        if (YouTubeIntents.canResolvePlayVideoIntent(getActivity().getApplicationContext())) {
            String mDeveloperKey = "AIzaSyB2iBP-uPngxmVCA-90wMng_icb07EIr4U";
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), mDeveloperKey, selectedTrailerVO.getKey());
            startActivity(intent);
        } else {
            String videoId = selectedTrailerVO.getKey();

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                startActivity(intent);
            }
        }
    }

    @Click(R.id.item_movie_vote_favorite_checkbox)
    void onMovieFavoriteClicked(View clickedView) {
        Log.i(LOG_TAG, "onMovieFavoriteClicked");
        int noticeMsg;
        boolean isChecked = ((CheckBox) clickedView).isChecked();

        if (isChecked) {
            Cursor c = getActivity().getContentResolver().query(MovieORMContract.CONTENT_URI, null,
                    "_ID = ?", new String[]{String.valueOf(mMovieVO.getId())}, null);

            if (c.getCount() <= 0) {
                ContentValues values = new ContentValues();
                values.clear();
                values.put(MovieORMContract._ID, mMovieVO.getId());
                values.put(MovieORMContract.TITLE, mMovieVO.getTitle());
                values.put(MovieORMContract.PLOTSYNOPSIS, mMovieVO.getPlotSynopsis());
                values.put(MovieORMContract.RELEASEDATE, mMovieVO.getReleaseDate());
                values.put(MovieORMContract.THUMBNAILSURL, mMovieVO.getThumbnailsURL());
                values.put(MovieORMContract.VOTEAVERAGE, mMovieVO.getVoteAverage());
                getActivity().getContentResolver().insert(MovieORMContract.CONTENT_URI, values);

            } else {
                mCbFavorite.setChecked(false);

                // Temporary
                getActivity().getContentResolver().delete(MovieORMContract.CONTENT_URI,
                        "_ID = ?", new String[]{String.valueOf(mMovieVO.getId())});

                noticeMsg = R.string.detail_msg_favorite_existed;
            }

            noticeMsg = R.string.detail_msg_favorite_added;
        } else {
            noticeMsg = R.string.detail_msg_favorite_removed;
        }

        if (getView() != null) {
            Snackbar.make(getView(), noticeMsg, Snackbar.LENGTH_SHORT).show();
        }
    }
}
