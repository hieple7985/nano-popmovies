package hieplt.popularmovie.models.gsons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HiepLT on 11/8/15.
 */
public class DiscoverMovieGSON {

    @SerializedName("page")
    public int mPage = 0;

    @SerializedName("results")
    public List<DiscoverResult> mResults;

    public class DiscoverResult {

        @SerializedName("adult")
        public boolean mAdult = false;

        @SerializedName("backdrop_path")
        public String mBackdropPath;

        @SerializedName("genre_ids")
        public int[] mGenreIds;

        @SerializedName("id")
        public int mId = 0;

        @SerializedName("original_language")
        public String mOriginalLanguage;

        @SerializedName("original_title")
        public String mOriginalTitle;

        @SerializedName("overview")
        public String mOverview;

        @SerializedName("release_date")
        public String mReleaseDate;

        @SerializedName("poster_path")
        public String mPosterPath;

        @SerializedName("popularity")
        public String mPopularity;

        @SerializedName("title")
        public String mTitle;

        @SerializedName("video")
        public boolean mVideo = false;

        @SerializedName("vote_average")
        public double mVoteAverage = 0;

        @SerializedName("vote_count")
        public int mVoteCount = 0;
    }

    @SerializedName("total_pages")
    public int mTotalPage = 0;

    @SerializedName("total_results")
    public int mTotalResult = 0;
}
