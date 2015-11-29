package hieplt.popularmovie.services.rests.youtube;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import hieplt.popularmovie.bases.ServiceBuilderBase;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by HiepLT on 11/18/15.
 */
@EBean(scope = EBean.Scope.Singleton)
/**
 * For future extension
 */
public class YouTubeBuilder extends ServiceBuilderBase {

    private YouTubeService mService;

    @AfterInject
    void init() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(mYouTubeEndPoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        mService = restAdapter.create(YouTubeService.class);
    }

    public YouTubeService getService() {
        return mService;
    }
}
