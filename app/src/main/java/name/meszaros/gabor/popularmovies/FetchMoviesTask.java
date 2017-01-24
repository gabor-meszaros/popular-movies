package name.meszaros.gabor.popularmovies;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gabor on 1/24/2017.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private static final String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";

    private Listener mListener;

    private TheMovieDbService mTheMovieDbService;

    interface Listener {
        void onFetchMoviesFinished(Movie[] movies);
    }

    public FetchMoviesTask(Listener listener) {
        mListener = listener;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;

        mTheMovieDbService = retrofit.create(theMovieDbServiceDefinition);
    }

    @Override
    protected Movie[] doInBackground(Void... params) {
        final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
        final Call<MovieListResponse> popularMoviesCall = mTheMovieDbService.getPopularMovies(apiKey);

        try {
            final Response<MovieListResponse> response = popularMoviesCall.execute();
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
            Log.w(LOG_TAG, "Nobody is listening for FetchMoviesTask.");
        }
        mListener.onFetchMoviesFinished(movies);
    }
}
