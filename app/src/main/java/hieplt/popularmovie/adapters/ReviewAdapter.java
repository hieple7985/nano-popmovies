package hieplt.popularmovie.adapters;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hieplt.popularmovie.R;
import hieplt.popularmovie.models.vos.ReviewVO;

/**
 * Created by HiepLT on 11/8/15.
 */
public class ReviewAdapter extends ArrayAdapter<ReviewVO> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private ViewHolder mHolder;

    /**
     * This is a custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param reviewVOes     A List of TrailerVOes objects to display in a list
     */
    public ReviewAdapter(Activity context, List<ReviewVO> reviewVOes) {
        super(context, 0, reviewVOes);
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
        ReviewVO reviewVO = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_movie_review, parent, false);

            mHolder = new ViewHolder();
            mHolder.mTvAuthor = (TextView) convertView.findViewById(R.id.item_movie_review_author);
            mHolder.mTvContent = (TextView) convertView.findViewById(R.id.item_movie_review_content);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.mTvAuthor.setText(reviewVO.getAuthor());
        mHolder.mTvContent.setText(Html.fromHtml(reviewVO.getContent()));

        return convertView;
    }

    private class ViewHolder {
        TextView mTvAuthor;
        TextView mTvContent;
    }
}
