package hieplt.popularmovie.bases;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import hieplt.popularmovie.R;

/**
 * Created by HiepLT on 11/15/15.
 */
public class PopMovieActivityBase extends AppCompatActivity {

    protected void onSetupSupportActionBar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
}
