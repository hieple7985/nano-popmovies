package hieplt.popularmovie.bases;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import hieplt.popularmovie.R;

/**
 * Created by HiepLT on 11/15/15.
 */
public class PopMovieFragmentBase extends Fragment {

    protected Activity mActivity;

    protected void onSetupSupportActionBar(Toolbar mToolbar) {
        // setSupportActionBar(mToolbar);
        // getActivity().set
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.menu_main, menu);
    }
}
