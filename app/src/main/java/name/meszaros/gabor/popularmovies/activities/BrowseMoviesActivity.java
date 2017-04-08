package name.meszaros.gabor.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;
import name.meszaros.gabor.popularmovies.network.FetchMoviesTask;
import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.adapters.MoviesAdapter;
import name.meszaros.gabor.popularmovies.R;

public class BrowseMoviesActivity extends AppCompatActivity
        implements FetchMoviesTask.Listener, MoviesAdapter.OnClickListener {

    private static final String LOG_TAG = BrowseMoviesActivity.class.getSimpleName();

    private static final String SAVED_MOVIES_KEY = "saved-movies-key";

    @BindView(R.id.text_error_display)
    TextView mErrorDisplayTextView;

    @BindView(R.id.progress_bar_load_movies)
    ProgressBar mLoadProgressBar;

    @BindView(R.id.recycler_movies)
    RecyclerView mMoviesRecyclerView;

    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_movies);

        ButterKnife.bind(this);

        final MoviesAdapter.OnClickListener listener = this;
        mAdapter = new MoviesAdapter(listener);

        mMoviesRecyclerView.setAdapter(mAdapter);

        final Context context = this;
        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);

        mMoviesRecyclerView.setLayoutManager(layoutManager);

        if (null == savedInstanceState) {
            loadMovies(FetchMoviesTask.LIST_POPULAR);
        } else {
            final Movie[] savedMovies =
                    (Movie[]) savedInstanceState.getParcelableArray(SAVED_MOVIES_KEY);
            mAdapter.setMovies(savedMovies);
            showMoviesList();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final Movie[] movies = mAdapter.getMovies();
        outState.putParcelableArray(SAVED_MOVIES_KEY, movies);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_browse_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_switch_popular:
                loadMovies(FetchMoviesTask.LIST_POPULAR);
                break;
            case R.id.menu_switch_highest_rated:
                loadMovies(FetchMoviesTask.LIST_TOP_RATED);
                break;
            case R.id.menu_switch_favorites:
                final Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                mAdapter.setMovies(cursor);
                break;
            default:
                Log.w(LOG_TAG, "Menu selection is not handled. ItemId: " + itemId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchMoviesFinished(final Movie[] movies) {
        final boolean moviesDisplayed = (0 != mAdapter.getItemCount());
        final boolean newMoviesAvailable = (null != movies);
        if (newMoviesAvailable) {
            showMoviesList();
            mAdapter.setMovies(movies);
            // We don't need to notify the Adapter's listeners as the setMovies()
            // had already done that for us
        } else { // No new movies available
            if (moviesDisplayed) {
                Log.w(LOG_TAG, "No new data available to refresh movies list. Kept old movies.");
            } else { // There are no movies in our RecyclerView
                showErrorDisplay();
            }
        }
    }

    private void loadMovies(final int listType) {
        showLoadProgressBar();
        final FetchMoviesTask.Listener listener = this;
        new FetchMoviesTask(listener, listType).execute();
    }

    private void showErrorDisplay() {
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        mErrorDisplayTextView.setVisibility(View.VISIBLE);
    }

    private void showLoadProgressBar() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorDisplayTextView.setVisibility(View.INVISIBLE);

        mLoadProgressBar.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        mErrorDisplayTextView.setVisibility(View.INVISIBLE);
        mLoadProgressBar.setVisibility(View.INVISIBLE);

        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieItemClick(final Movie movie) {
        final Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
        startActivity(movieDetailsIntent);
    }
}
