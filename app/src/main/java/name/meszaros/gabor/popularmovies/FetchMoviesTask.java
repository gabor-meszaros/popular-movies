package name.meszaros.gabor.popularmovies;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Gabor on 1/24/2017.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private Listener mListener;

    interface Listener {
        void onFetchMoviesFinished(Movie[] movies);
    }

    public FetchMoviesTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Movie[] doInBackground(Void... params) {
        // Make it look like a real network call
        final int aSecondInMilliseconds = 1000;
        SystemClock.sleep(aSecondInMilliseconds);

        Movie[] dummyMovies = {
                new Movie("Rogue One", "https://images-na.ssl-images-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SY1000_SX675_AL_.jpg"),
                new Movie("The Dark Knight", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SY1000_CR0,0,675,1000_AL_.jpg"),
                new Movie("Pulp Fiction", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTkxMTA5OTAzMl5BMl5BanBnXkFtZTgwNjA5MDc3NjE@._V1_SY1000_CR0,0,673,1000_AL_.jpg"),
                new Movie("Passangers", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTk4MjU3MDIzOF5BMl5BanBnXkFtZTgwMjM2MzY2MDI@._V1_SY1000_CR0,0,675,1000_AL_.jpg"),
                new Movie("Forrest Gump", "https://images-na.ssl-images-amazon.com/images/M/MV5BYThjM2MwZGMtMzg3Ny00NGRkLWE4M2EtYTBiNWMzOTY0YTI4XkEyXkFqcGdeQXVyNDYyMDk5MTU@._V1_SY1000_CR0,0,757,1000_AL_.jpg"),
                new Movie("Moana", "https://images-na.ssl-images-amazon.com/images/M/MV5BMjI4MzU5NTExNF5BMl5BanBnXkFtZTgwNzY1MTEwMDI@._V1_SY1000_CR0,0,674,1000_AL_.jpg"),
                new Movie("Fight Club", "https://images-na.ssl-images-amazon.com/images/M/MV5BMzc1YmU2ZjEtYWIwMC00ZjM3LWI0NTctMDVlNGQ3YmYwMzE5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SY999_CR0,0,704,999_AL_.jpg"),
                new Movie("Frozen", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SY1000_CR0,0,674,1000_AL_.jpg"),
                new Movie("Arrival", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTExMzU0ODcxNDheQTJeQWpwZ15BbWU4MDE1OTI4MzAy._V1_SY1000_CR0,0,640,1000_AL_.jpg")
        };

        return dummyMovies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        reportResultToTheCaller(movies);
    }

    @Override
    protected void onCancelled() {
        reportResultToTheCaller(null);
    }

    private void reportResultToTheCaller(Movie[] movies) {
        if (null == mListener) {
            Log.w(LOG_TAG, "Nobody is listening for FetchMoviesTask.");
        }
        mListener.onFetchMoviesFinished(movies);
    }
}
