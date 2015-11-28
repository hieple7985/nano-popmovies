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
    // API JSON KEY
    public static final String  TMDB_API_KEY            = "09bfc8564818434e8872ac6c06c1ae25";

    // TMDB Base URL
    public static final String  TMDB_IMAGE_BASE_URL     = "http://image.tmdb.org/t/p/";

    // ------------------------------------------------------------------------
    // API CODE STATUSES
    // Ref's Link: https://www.themoviedb.org/documentation/api/status-codes
    // ------------------------------------------------------------------------
    // STATUS CODES
    public static final int     TMBD_STATUS_CODE_1_200    = 200;
    public static final int     TMBD_STATUS_CODE_2_501    = 501;
    public static final int     TMBD_STATUS_CODE_3_401    = 401;
    public static final int     TMBD_STATUS_CODE_4_405    = 405;
    public static final int     TMBD_STATUS_CODE_5_422    = 422;
    public static final int     TMBD_STATUS_CODE_6_404    = 404;
    public static final int     TMBD_STATUS_CODE_7_401    = 401;
    public static final int     TMBD_STATUS_CODE_8_403    = 403;
    public static final int     TMBD_STATUS_CODE_9_503    = 503;
    public static final int     TMBD_STATUS_CODE_10_401   = 401;

    // STATUS MESSAGES
    public static final String  TMBD_STATUS_MSG_1_200     = "Success";
    public static final String  TMBD_STATUS_MSG_2_501     = "Invalid service: this service does not exist.";
    public static final String  TMBD_STATUS_MSG_3_401     = "Authentication failed: You do not have permissions to access the service.";
    public static final String  TMBD_STATUS_MSG_4_405     = "Invalid format: This service doesn't exist in that format.";
    public static final String  TMBD_STATUS_MSG_5_422     = "Invalid parameters: Your request parameters are incorrect.";
    public static final String  TMBD_STATUS_MSG_6_404     = "Invalid id: The pre-requisite id is invalid or not found.";
    public static final String  TMBD_STATUS_MSG_7_401     = "Invalid API key: You must be granted a valid key.";
    public static final String  TMBD_STATUS_MSG_8_403     = "Duplicate entry: The data you tried to submit already exists.";
    public static final String  TMBD_STATUS_MSG_9_503     = "Service offline: This service is temporarily offline, try again later.";
    public static final String  TMBD_STATUS_MSG_10_401    = "Suspended API key: Access to your account has been suspended, contact TMDb.";

    // For future

//            11	500	Internal error: Something went wrong, contact TMDb.
//            12	201	The item/record was updated successfully.
//            13	200	The item/record was deleted successfully.
//            14	401	Authentication failed.
//            15	500	Failed.

//    16	401	Device denied.
//            17	401	Session denied.
//            18	400	Validation failed.
//            19	406	Invalid accept header.
//    20	422	Invalid date range: Should be a range no longer than 14 days.

//    21	200	Entry not found: The item you are trying to edit cannot be found.
//            22	400	Invalid page: Pages start at 1 and max at 1000. They are expected to be an integer.
//    23	400	Invalid date: Format needs to be YYYY-MM-DD.
//    24	504	Your request to the backend server timed out. Try again.
//            25	429	Your request count (#) is over the allowed limit of (40).

//            26	400	You must provide a username and password.
//    27	400	Too many append to response objects: The maximum number of remote calls is 20.
//            28	400	Invalid timezone: Please consult the documentation for a valid timezone.
//    29	400	You must confirm this action: Please provide a confirm=true parameter.
//    30	401	Invalid username and/or password: You did not provide a valid login.

//    31	401	Account disabled: Your account is no longer active. Contact TMDb if this is an error.
//    32	401	Email not verified: Your email address has not been verified.
//    33	401	Invalid request token: The request token is either expired or invalid.
//            34	401	The resource you requested could not be found.

}
