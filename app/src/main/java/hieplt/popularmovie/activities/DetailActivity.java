package hieplt.popularmovie.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
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
import hieplt.popularmovie.bases.PopMovieActivityBase;
import hieplt.popularmovie.commons.Constants;
import hieplt.popularmovie.models.gsons.MovieReviewGSON;
import hieplt.popularmovie.models.gsons.MovieTrailerGSON;
import hieplt.popularmovie.models.vos.MovieVO;
import hieplt.popularmovie.models.vos.ReviewVO;
import hieplt.popularmovie.models.vos.TrailerVO;
import hieplt.popularmovie.services.rests.tmdb.TMDBMovieBuilder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@Fullscreen
@EActivity(R.layout.activity_detail)
public class DetailActivity extends PopMovieActivityBase {

    private final static String LOG_TAG = DetailActivity.class.getSimpleName();

    // Context
    private Context mContext;

    // Services
    @Bean
    TMDBMovieBuilder mTMDBMovieBuilder;

    // Views
    @ViewById(R.id.item_movie_title)
    TextView mTvTitle;

    @ViewById(R.id.item_movie_release_date_value)
    TextView mTvReleaseDate;

    @ViewById(R.id.item_movie_poster)
    ImageView mIvPoster;

    @ViewById(R.id.item_movie_vote_average_value)
    TextView mTvVoteAverage;

    @ViewById(R.id.item_movie_plot_synopsis)
    TextView mTvPlotSynopsis;

    @ViewById(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @ViewById(R.id.lv_movie_trailers)
    ListView mlvTrailers;

    @ViewById(R.id.lv_movie_reviews)
    ListView mlvReviews;

    // Adapters
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;

    // Data Value Object
    List<TrailerVO> mTrailerVOs;
    List<ReviewVO> mReviewVOs;

    @AfterViews
    void initViews() {

        mContext = getApplicationContext();

        onSetupSupportActionBar(mToolbar);

        // Custom for detail movie screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_name_discover_detail);

        MovieVO movieVO = Parcels.unwrap(getIntent().getParcelableExtra(Constants.EXTRA_DISCOVER_MOVIE));
        mTvTitle.setText(movieVO.getTitle());
        Picasso.with(getApplicationContext()).load(movieVO.getThumbnailsURL()).into(mIvPoster);
        mTvReleaseDate.setText(movieVO.getReleaseDate());
        mTvVoteAverage.setText(movieVO.getVoteAverage());
        mTvPlotSynopsis.setText(movieVO.getPlotSynopsis());

        if (mTrailerAdapter == null) {
            mTrailerAdapter = new TrailerAdapter(this, new ArrayList<TrailerVO>());
        }
        mlvTrailers.setAdapter(mTrailerAdapter);

        if (mReviewAdapter == null) {
            mReviewAdapter = new ReviewAdapter(this, new ArrayList<ReviewVO>());
        }
        mlvReviews.setAdapter(mReviewAdapter);

        // Load more detailed data.
        doLoadTrailersInBackground(movieVO.getId());
        doLoadReviewsInBackground(movieVO.getId());
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

    @ItemClick(R.id.lv_movie_trailers)
    void onMovieTrailerItemClicked(TrailerVO selectedTrailerVO) {

        if (YouTubeIntents.canResolvePlayVideoIntent(getApplicationContext())) {
            String mDeveloperKey = "AIzaSyB2iBP-uPngxmVCA-90wMng_icb07EIr4U";
            Intent intent = YouTubeStandalonePlayer
                .createVideoIntent(this, mDeveloperKey, selectedTrailerVO.getKey());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @OptionsItem(android.R.id.home)
    void onHomeSelected() {
        onBackPressed();
    }
}
