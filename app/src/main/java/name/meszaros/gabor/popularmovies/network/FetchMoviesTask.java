package name.meszaros.gabor.popularmovies.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.models.MovieListResponse;
import name.meszaros.gabor.popularmovies.utils.TheMovieDbUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Background task to fetch movies from The Movie DB service.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

    public static final int LIST_POPULAR = 1;
    public static final int LIST_TOP_RATED = 2;

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private Listener mListener;

    private int mListType;

    public interface Listener {
        void onFetchMoviesFinished(Movie[] movies);
    }

    public FetchMoviesTask(final Listener listener, final int listType) {
        checkListener(listener);
        checkListType(listType);

        mListener = listener;
        mListType = listType;
    }

    private void checkListener(final Listener listener) {
        if (null == listener) {
            final String errorMessage = "The listener cannot be null." +
                    " It is needed for passing the result back.";
            Log.e(LOG_TAG, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void checkListType(final int listType) {
        switch (listType) {
            case LIST_POPULAR:
            case LIST_TOP_RATED:
                return;
            default:
                final String errorMessage = "Unknown list type for FetchMoviesTask. List type: "
                        + listType;
                Log.e(LOG_TAG, errorMessage);
                throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    protected Movie[] doInBackground(final Void... params) {
        final Call<MovieListResponse> call = getMovieListCall(mListType);
        if (null == call) return null;

        try {
            final Response<MovieListResponse> response = call.execute();
            if (response.isSuccessful()) {
                final List<Movie> movies = response.body().getMovies();

                return movies.toArray(new Movie[0]);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "The Movie DB service connection error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final Movie[] movies) {
        if (null == mListener) {
            Log.wtf(LOG_TAG, "Nobody is listening for FetchMoviesTask result.");
        } else {
            mListener.onFetchMoviesFinished(movies);
        }
    }

    private Call<MovieListResponse> getMovieListCall(final int listType) {
        Call<MovieListResponse> call = null;
        switch (listType) {
            case LIST_POPULAR:
                call = TheMovieDbUtils.getPopularMovies();
                break;
            case LIST_TOP_RATED:
                call = TheMovieDbUtils.getTopRatedMovies();
                break;
            default:
                Log.wtf(LOG_TAG, "Unknown list type for FetchMoviesTask. List type: " + listType);
        }

        return call;
    }
}
