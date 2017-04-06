package name.meszaros.gabor.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.models.Trailer;

/**
 * Adapter for providing trailers for the RecyclerView.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private final static String LOG_TAG = TrailersAdapter.class.getSimpleName();

    public interface OnClickListener {
        void onTrailerItemClick(Trailer trailer);
    }

    private Trailer[] mTrailers;

    private TrailersAdapter.OnClickListener mListener;

    public TrailersAdapter(final TrailersAdapter.OnClickListener listener) {
        mTrailers = null;
        mListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_trailer;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final TrailerViewHolder viewHolder = new TrailerViewHolder(context, view);

        return viewHolder;
    }

    public void setTrailers(final Trailer[] reviews) {
        this.mTrailers = reviews;
        notifyDataSetChanged();
    }

    public Trailer[] getTrailers() {
        return mTrailers;
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, final int position) {
        final Trailer trailer = mTrailers[position];
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.length;
    }

    /**
     * View holder for trailer items in the RecyclerView.
     */
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Context mContext;

        private ImageView mTrailerThumbnail;

        public TrailerViewHolder(final Context context, final View itemView) {
            super(itemView);

            mContext = context;
            mTrailerThumbnail =
                   (ImageView) itemView.findViewById(R.id.image_trailer_thumbnail);
            mTrailerThumbnail.setOnClickListener(this);
        }

        public void bind(final Trailer trailer) {
            final String thumbnailLinkToBind = trailer.getThumbnailLink();
            Picasso.with(mContext).load(thumbnailLinkToBind).into(mTrailerThumbnail);
        }

        @Override
        public void onClick(final View view) {
            if (null != mTrailers) {
                final int position = getAdapterPosition();
                final Trailer trailer = mTrailers[position];
                mListener.onTrailerItemClick(trailer);
            } else {
                Log.wtf(LOG_TAG, "OnClick handler call with empty trailers list.");
            }
        }
    }
}
