package name.meszaros.gabor.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Adapter for providing movies for the RecyclerView.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final Movie[] sDummyMovies = {
            new Movie("Rogue One"),
            new Movie("The Dark Knight"),
            new Movie("Pulp Fiction"),
            new Movie("Passangers"),
            new Movie("Forrest Gump"),
            new Movie("Moana"),
            new Movie("Fight Club"),
            new Movie("Frozen"),
            new Movie("Arrival")
    };

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_movie;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return sDummyMovies.length;
    }

    /**
     * View holder for movie items in the RecyclerView.
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView movieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_item_title);
        }

        public void bind(int position) {
            final Movie movieToBind = sDummyMovies[position];
            final String titleToBind = movieToBind.getTitle();
            movieTitle.setText(titleToBind);
        }
    }
}