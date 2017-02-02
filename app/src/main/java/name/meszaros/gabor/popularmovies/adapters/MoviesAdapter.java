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
import name.meszaros.gabor.popularmovies.models.Movie;

/**
 * Adapter for providing movies for the RecyclerView.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private final static String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public interface OnClickListener {
        void onMovieItemClick(Movie movie);
    }

    private Movie[] mMovies;

    private MoviesAdapter.OnClickListener mListener;

    public MoviesAdapter(MoviesAdapter.OnClickListener listener) {
        mMovies = null;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_movie;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final MovieViewHolder viewHolder = new MovieViewHolder(context, view);

        return viewHolder;
    }

    public void setMoviesData(Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mMovies[position];
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.length;
    }

    /**
     * View holder for movie items in the RecyclerView.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePoster;

        private final Context mContext;

        public MovieViewHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;
            mMoviePoster = (ImageView) itemView.findViewById(R.id.image_movie_item_poster);
            mMoviePoster.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            final String posterLinkToBind = movie.getPosterLink();
            Picasso.with(mContext).load(posterLinkToBind).into(mMoviePoster);
        }

        @Override
        public void onClick(View view) {
            if (null != mMovies) {
                final int position = getAdapterPosition();
                final Movie movie = mMovies[position];
                mListener.onMovieItemClick(movie);
            } else {
                Log.wtf(LOG_TAG, "OnClick handler call with empty movie list.");
            }
        }
    }
}
