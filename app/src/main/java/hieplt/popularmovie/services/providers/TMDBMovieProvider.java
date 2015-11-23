package hieplt.popularmovie.services.providers;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;

import hieplt.popularmovie.services.daos.DatabaseHelper;

/**
 * Created by HiepLT on11/21/15.
 */
public class TMDBMovieProvider extends OrmLiteSimpleContentProvider<DatabaseHelper> {

//    private static final int ROUTE_CARDS        = 1;
//    private static final int ROUTE_CARDS_ID     = 2;
//
//    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        static {
//            sUriMatcher.addURI(AUTHORITY, "movies", ROUTE_CARDS);
//            sUriMatcher.addURI(AUTHORITY, "movies/*", ROUTE_CARDS_ID);
//    }

//    @Bean
//    DatabaseHelper mDatabaseHelper;

    // Creates a UriMatcher object.
    // private static final UriMatcher sUriMatcher;
    // sUriMatcher.addURI("com.example.app.provider", "table3", 1);

//    @SystemService
//    NotificationManager notificationManager;

    // @Bean
    // MyEnhancedDatastore datastore;

//    @OrmLiteDao(helper = DatabaseHelper.class, model = MovieORM.class)
//    RuntimeExceptionDao<MovieORM, Long> movieRuntimeDao;
//
//    @OrmLiteDao(helper = DatabaseHelper.class, model = MovieORM.class)
//    Dao<MovieORM, Integer> movieDao;

    //    @UiThread
//    void showToast() {
//        Toast.makeText(getContext().getApplicationContext(), "Hello World!", Toast.LENGTH_LONG).show();
//    }


    @Override
    public boolean onCreate() {


        setMatcherController(new MatcherController()
//            .add(MovieORM.class, MimeTypeVnd.SubType.DIRECTORY, "", MovieORM.class)
//            .add(MovieORM.class, MimeTypeVnd.SubType.ITEM, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
        );

        return true;
    }

    @Override
    protected Class<DatabaseHelper> getHelperClass() {
        return DatabaseHelper.class;
    }
}
