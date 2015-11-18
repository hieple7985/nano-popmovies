package hieplt.popularmovie.models.gsons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HiepLT on 11/8/15.
 */
public class MovieReviewGSON {

    @SerializedName("id")
    public int mId = 0;

    @SerializedName("page")
    public int mPage = 0;

    @SerializedName("results")
    public List<ReviewResult> mResults;

    public class ReviewResult {

        @SerializedName("id")
        public String mId;

        @SerializedName("author")
        public String mAuthor;

        @SerializedName("content")
        public String mContent;

        @SerializedName("url")
        public String mUrl;
    }

    @SerializedName("total_pages")
    public int mTotalPage = 0;

    @SerializedName("total_results")
    public int mTotalResult = 0;
}
