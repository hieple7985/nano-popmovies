package hieplt.popularmovie;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by HiepLT on 11/17/15.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();

        final Context context = getApplicationContext();

        Stetho.initializeWithDefaults(this);

//        Stetho.initialize(Stetho.newInitializerBuilder(context)
//                .enableDumpapp(new DumperPluginsProvider() {
//                    @Override
//                    public Iterable<DumperPlugin> get() {
//                        return new Stetho.DefaultDumperPluginsBuilder(context)
//                                .provide(new MyDumperPlugin())
//                                .finish();
//                }
//            })
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
//            .build());

//        OkHttpClient client = new OkHttpClient();
//        client.networkInterceptors().add(new StethoInterceptor());
    }
}
