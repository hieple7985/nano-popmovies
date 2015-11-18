package hieplt.popularmovie.models.vos;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Created by HiepLT on 11/8/15.
 */
@Parcel(Parcel.Serialization.BEAN)
public class MovieVO {

    public class ThumbnailSize {

        public final static String mOriginal    = "original";
        public final static String mW92         = "w92";
        public final static String mW154        = "w154";
        public final static String mW185        = "w185";
        public final static String mW342        = "w342";
        public final static String mW500        = "w500";
        public final static String mW780        = "w780";
    }

    @ParcelProperty("id")
    int mId;
    @ParcelProperty("title")
    String mTitle;
    @ParcelProperty("releaseDate")
    String mReleaseDate;
    @ParcelProperty("thumbnailsUrl")
    String mThumbnailsURL;
    @ParcelProperty("voteAverage")
    String mVoteAverage;
    @ParcelProperty("plotSynopsis")
    String mPlotSynopsis;

    public MovieVO() {}

    @ParcelConstructor
    public MovieVO(int id, String title, String releaseDate, String thumbnailsUrl
            , String voteAverage, String plotSynopsis) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mThumbnailsURL = thumbnailsUrl;
        this.mVoteAverage = voteAverage;
        this.mPlotSynopsis = plotSynopsis;
    }

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    @ParcelProperty("id")
    public int getId() {
        return mId;
    }

    @ParcelProperty("id")
    public void setId(int mId) {
        this.mId = mId;
    }

    @ParcelProperty("title")
    public String getTitle() {
        return mTitle;
    }

    @ParcelProperty("title")
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @ParcelProperty("releaseDate")
    public String getReleaseDate() {
        return mReleaseDate;
    }

    @ParcelProperty("releaseDate")
    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @ParcelProperty("thumbnailsUrl")
    public String getThumbnailsURL() {
        return mThumbnailsURL;
    }

    @ParcelProperty("thumbnailsUrl")
    public void setThumbnailsURL(String mThumbnailsURL) {
        this.mThumbnailsURL = mThumbnailsURL;
    }

    @ParcelProperty("voteAverage")
    public String getVoteAverage() {
        return mVoteAverage;
    }

    @ParcelProperty("voteAverage")
    public void setVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    @ParcelProperty("plotSynopsis")
    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    @ParcelProperty("plotSynopsis")
    public void setPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }
}
