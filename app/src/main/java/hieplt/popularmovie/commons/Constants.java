package hieplt.popularmovie.commons;

import hieplt.popularmovie.BuildConfig;

/**
 * Created by HiepLT on 11/2/15.
 */
public class Constants {

    // ------------------------------------------------------------------------
    // ORM Configuration
    // ------------------------------------------------------------------------
    public static final String  AUTHORITY               = BuildConfig.APPLICATION_ID;
    public static final String  CONTENT_MINE_TYPE       = BuildConfig.APPLICATION_ID + ".provider";

    // ------------------------------------------------------------------------
    // Intent Data Key
    // ------------------------------------------------------------------------
    public static final String  EXTRA_DISCOVER_MOVIE    = "EXTRA_DISCOVER_MOVIE";
    public static final String  EXTRA_DISCOVER_MODE     = "EXTRA_DISCOVER_MODE";

    // ------------------------------------------------------------------------
    // YouTube
    public static final String  YOU_TUBE_VIDEO_URL      = "http://www.youtube.com/watch?v=";

    // ------------------------------------------------------------------------
    // API JSON KEY
    public static final String  TMDB_API_KEY            = "YOUR_TMDB_API_KEY_HERE";

    // TMDB Base URL
    public static final String  TMDB_IMAGE_BASE_URL     = "http://image.tmdb.org/t/p/";
}
