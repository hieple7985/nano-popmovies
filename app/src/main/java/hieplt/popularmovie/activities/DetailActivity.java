package hieplt.popularmovie.activities;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import hieplt.popularmovie.R;
import hieplt.popularmovie.bases.PopMovieActivityBase;
import hieplt.popularmovie.commons.Constants;
import hieplt.popularmovie.models.vos.MovieVO;

@Fullscreen
@EActivity(R.layout.activity_detail)
public class DetailActivity extends PopMovieActivityBase {

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

    @AfterViews
    void initViews() {

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
