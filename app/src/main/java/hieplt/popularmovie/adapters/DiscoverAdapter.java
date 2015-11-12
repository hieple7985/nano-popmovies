package hieplt.popularmovie.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hieplt.popularmovie.R;
import hieplt.popularmovie.models.vos.MovieVO;

/**
 * Created by HiepLT on 11/8/15.
 */
public class DiscoverAdapter extends ArrayAdapter<MovieVO> {

    private static final String LOG_TAG = DiscoverAdapter.class.getSimpleName();

    private ViewHolder mHolder;

    /**
     * This is a custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movieVOSes     A List of MovieVOS objects to display in a list
     */
    public DiscoverAdapter(Activity context, List<MovieVO> movieVOSes) {
        super(context, 0, movieVOSes);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MovieVO object from the ArrayAdapter at the appropriate position
        MovieVO movieVO = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_discover_movie, parent, false);

            mHolder = new ViewHolder();
            mHolder.mIvPoster = (ImageView) convertView.findViewById(R.id.discovert_item_movie_thumnail);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(movieVO.mThumbnailsURL).into(mHolder.mIvPoster);

        convertView.setTag(mHolder);

        return convertView;
    }

    private class ViewHolder {
        ImageView mIvPoster = null;
    }
}
