package hieplt.popularmovie.models.gsons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HiepLT on 11/8/15.
 */
public class MovieTrailerGSON {

    @SerializedName("id")
    public int mId = 0;

    @SerializedName("results")
    public List<TrailerResult> mResults;

    public class TrailerResult {

        @SerializedName("id")
        public String mId;

        @SerializedName("iso_639_1")
        public String mIso6391;

        @SerializedName("key")
        public String mKey;

        @SerializedName("name")
        public String mName;

        @SerializedName("site")
        public String mSite;

        @SerializedName("size")
        public int mSize = 0;

        @SerializedName("type")
        public String mType;
    }
}
