package name.meszaros.gabor.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;
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

    public MoviesAdapter(final MoviesAdapter.OnClickListener listener) {
        mMovies = null;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_movie;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final MovieViewHolder viewHolder = new MovieViewHolder(context, view);

        return viewHolder;
    }

    public void setMovies(final Movie[] movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    public void setMovies(final Cursor cursor) {
        if (null != cursor && cursor.getCount() != 0) {
            final Movie[] movies = new Movie[cursor.getCount()];
            int currentMovieIndex = 0;
            while (cursor.moveToNext()) {
                final Movie movie = getMovieFromCursor(cursor);
                movies[currentMovieIndex++] = movie;
            }
            setMovies(movies);
        }
    }

    private Movie getMovieFromCursor(final Cursor cursor) {
        if (null != cursor && cursor.getCount() != 0) {
            final Movie movie = new Movie();

            movie.setId(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));

            final int originalTitleColumnIndex =
                    cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE);
            movie.setOriginalTitle(cursor.getString(originalTitleColumnIndex));

            movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS)));

            final int userRatingColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING);
            movie.setUserRating(cursor.getString(userRatingColumnIndex));

            final int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            final int epochMilliseconds = cursor.getInt(releaseDateColumnIndex);
            movie.setReleaseDate(new Date(epochMilliseconds));

            final int posterPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
            movie.setPosterPath(cursor.getString(posterPathColumnIndex));

            return movie;
        } else {
            return null;
        }
    }

    public Movie[] getMovies() {
        return mMovies;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
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

        public MovieViewHolder(final Context context, final View itemView) {
            super(itemView);

            mContext = context;
            mMoviePoster = (ImageView) itemView.findViewById(R.id.image_movie_item_poster);
            mMoviePoster.setOnClickListener(this);
        }

        public void bind(final Movie movie) {
            final String posterPathToBind = movie.getPosterPath();
            Picasso.with(mContext).load(posterPathToBind).into(mMoviePoster);
        }

        @Override
        public void onClick(final View view) {
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
