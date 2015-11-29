package hieplt.popularmovie.services.rests.tmdb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import hieplt.popularmovie.bases.ServiceBuilderBase;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by HiepLT on 11/18/14.
 */
@EBean(scope = EBean.Scope.Singleton)
public class TMDBDiscoverBuilder extends ServiceBuilderBase {

    private TMDBDiscoverService mService;

    @AfterInject
    void init() {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(mTMDBEndPoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        mService = restAdapter.create(TMDBDiscoverService.class);
    }

    public TMDBDiscoverService getService() {
        return mService;
    }
}
