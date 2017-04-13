package name.meszaros.gabor.popularmovies.activities;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;
import name.meszaros.gabor.popularmovies.network.FetchMoviesTask;
import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.adapters.MoviesAdapter;
import name.meszaros.gabor.popularmovies.R;

public class BrowseMoviesActivity extends AppCompatActivity
        implements FetchMoviesTask.Listener, MoviesAdapter.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = BrowseMoviesActivity.class.getSimpleName();

    private static final String SAVED_MOVIES_KEY = "saved-movies-key";
    private static final String SAVED_SELECTED_MOVIE_LIST_KEY = "saved-selected-movie-list-key";

    private static final int ID_LOADER_FAVORITE_MOVIES = 1;

    @BindView(R.id.text_error_display)
    TextView mErrorDisplayTextView;

    @BindView(R.id.progress_bar_load_movies)
    ProgressBar mLoadProgressBar;

    @BindView(R.id.recycler_movies)
    RecyclerView mMoviesRecyclerView;

    private MoviesAdapter mAdapter;

    private int mSelectedMovieList;

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
            final ArrayList<Movie> savedMovieList = savedInstanceState.getParcelableArrayList(SAVED_MOVIES_KEY);
            final Movie[] savedMovieArray = savedMovieList.toArray(new Movie[0]);
            mAdapter.setMovies(savedMovieArray);
            mSelectedMovieList = savedInstanceState.getInt(SAVED_SELECTED_MOVIE_LIST_KEY);
            showMoviesList();
        }

        getSupportLoaderManager().initLoader(ID_LOADER_FAVORITE_MOVIES, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final ArrayList<Movie> movies = new ArrayList(Arrays.asList(mAdapter.getMovies()));
        outState.putParcelableArrayList(SAVED_MOVIES_KEY, movies);
        outState.putInt(SAVED_SELECTED_MOVIE_LIST_KEY, mSelectedMovieList);
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
                mSelectedMovieList = itemId;
                loadMovies(FetchMoviesTask.LIST_POPULAR);
                break;
            case R.id.menu_switch_highest_rated:
                mSelectedMovieList = itemId;
                loadMovies(FetchMoviesTask.LIST_TOP_RATED);
                break;
            case R.id.menu_switch_favorites:
                mSelectedMovieList = itemId;
                loadFavoriteMovies();
                break;
            default:
                Log.w(LOG_TAG, "Menu selection is not handled. ItemId: " + itemId);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteMovies() {
        showLoadProgressBar();

        final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                mAdapter.setMovies(cursor);

                if (0 != mAdapter.getItemCount()) {
                    showMoviesList();
                } else {
                    showErrorDisplay(R.string.error_no_movies);
                }

                if (null != cursor) cursor.close();
            }
        };

        final int anyId = 42; // We will not use it in the result handler function
        asyncQueryHandler.startQuery(anyId, null, MovieEntry.CONTENT_URI, null, null, null, null);
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
                showErrorDisplay(R.string.error_no_internet);
            }
        }
    }

    private void loadMovies(final int listType) {
        showLoadProgressBar();
        final FetchMoviesTask.Listener listener = this;
        new FetchMoviesTask(listener, listType).execute();
    }

    private void showErrorDisplay(final int messageStringResourceId) {
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        final String message = getString(messageStringResourceId);
        mErrorDisplayTextView.setText(message);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_LOADER_FAVORITE_MOVIES:
                try {
                    return new CursorLoader(this,
                            MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (R.id.menu_switch_favorites == mSelectedMovieList) {
            mAdapter.setMovies(data);
            if (mAdapter.getItemCount() != 0) {
                showMoviesList();
            } else {
                showErrorDisplay(R.string.error_no_movies);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (R.id.menu_switch_favorites == mSelectedMovieList) {
            mAdapter.setMovies((Cursor) null);
            showErrorDisplay(R.string.error_no_internet);
        }
    }
}
