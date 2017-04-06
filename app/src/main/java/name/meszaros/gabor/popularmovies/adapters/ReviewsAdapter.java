package name.meszaros.gabor.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.models.Review;

/**
 * Adapter for providing reviews for the RecyclerView.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private Review[] mReviews;

    public ReviewsAdapter() {
        mReviews = null;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_review;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    public void setReviews(final Review[] reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }

    public Review[] getReviews() {
        return mReviews;
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, final int position) {
        final Review review = mReviews[position];
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.length;
    }

    /**
     * View holder for review items in the RecyclerView.
     */
    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mReviewAuthor;
        private TextView mReviewContent;

        public ReviewViewHolder(final View itemView) {
            super(itemView);

            mReviewAuthor = (TextView) itemView.findViewById(R.id.text_review_author);
            mReviewContent = (TextView) itemView.findViewById(R.id.text_review_content);
        }

        public void bind(final Review review) {
            final String reviewAuthorToBind = review.getAuthor();
            mReviewAuthor.setText(reviewAuthorToBind);
            final String reviewContentToBind = review.getContent();
            mReviewContent.setText(reviewContentToBind);
        }
    }
}
