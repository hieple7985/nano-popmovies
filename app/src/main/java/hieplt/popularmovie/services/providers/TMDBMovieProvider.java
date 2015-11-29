package hieplt.popularmovie.services.providers;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

import hieplt.popularmovie.models.orms.MovieORM;
import hieplt.popularmovie.models.orms.MovieORMContract;
import hieplt.popularmovie.services.daos.DatabaseHelper;

/**
 * Created by HiepLT on11/21/15.
 */
public class TMDBMovieProvider extends OrmLiteSimpleContentProvider<DatabaseHelper> {

    @Override
    public boolean onCreate() {

        setMatcherController(new MatcherController()
            .add(MovieORM.class, MimeTypeVnd.SubType.DIRECTORY, "", MovieORMContract.CONTENT_URI_PATTERN_MANY)
            .add(MovieORM.class, MimeTypeVnd.SubType.ITEM, "#", MovieORMContract.CONTENT_URI_PATTERN_ONE)
        );

        return true;
    }

    @Override
    protected Class<DatabaseHelper> getHelperClass() {
        return DatabaseHelper.class;
    }
}
