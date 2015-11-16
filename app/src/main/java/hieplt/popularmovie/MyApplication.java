package hieplt.popularmovie;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by HiepLT on 11/17/15.
 */
public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

//        OkHttpClient client = new OkHttpClient();
//        client.networkInterceptors().add(new StethoInterceptor());
    }
}
