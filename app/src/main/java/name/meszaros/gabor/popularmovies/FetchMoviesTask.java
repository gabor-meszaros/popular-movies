package name.meszaros.gabor.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Background task to fetch movies from The Movie DB service.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

    public static final int LIST_POPULAR = 1;
    public static final int LIST_TOP_RATED = 2;

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private Listener mListener;

    private int mListType;

    private TheMovieDbService mTheMovieDbService;

    interface Listener {
        void onFetchMoviesFinished(Movie[] movies);
    }

    public FetchMoviesTask(Listener listener, int listType) {
        checkListener(listener);
        checkListType(listType);

        mListener = listener;
        mListType = listType;
        mTheMovieDbService = createTheMovieDbService();
    }

    private void checkListener(Listener listener) {
        if (null == listener) {
            final String errorMessage = "The listener cannot be null." +
                    " It is needed for passing the result back.";
            Log.e(LOG_TAG, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void checkListType(int listType) {
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

    private TheMovieDbService createTheMovieDbService() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;

        return retrofit.create(theMovieDbServiceDefinition);
    }

    @Override
    protected Movie[] doInBackground(Void... params) {
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
    protected void onPostExecute(Movie[] movies) {
        if (null == mListener) {
            Log.wtf(LOG_TAG, "Nobody is listening for FetchMoviesTask result.");
        } else {
            mListener.onFetchMoviesFinished(movies);
        }
    }

    private Call<MovieListResponse> getMovieListCall(int listType) {
        final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

        Call<MovieListResponse> call = null;
        switch (listType) {
            case LIST_POPULAR:
                call = mTheMovieDbService.getPopularMovies(apiKey);
                break;
            case LIST_TOP_RATED:
                call =  mTheMovieDbService.getTopRatedMovies(apiKey);
                break;
            default:
                Log.wtf(LOG_TAG, "Unknown list type for FetchMoviesTask. List type: " + listType);
        }

        return call;
    }
}
