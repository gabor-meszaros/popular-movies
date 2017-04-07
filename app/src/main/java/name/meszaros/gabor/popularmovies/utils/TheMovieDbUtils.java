package name.meszaros.gabor.popularmovies.utils;

import android.net.Uri;

import name.meszaros.gabor.popularmovies.BuildConfig;
import name.meszaros.gabor.popularmovies.models.MovieListResponse;
import name.meszaros.gabor.popularmovies.models.ReviewListResponse;
import name.meszaros.gabor.popularmovies.models.TrailerListResponse;
import name.meszaros.gabor.popularmovies.network.TheMovieDbService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheMovieDbUtils {

    private static final String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public static String getAbsolutePosterPath(final String relativePosterPath) {
        final Uri posterUri = Uri.parse(POSTER_PATH_BASE_URL).buildUpon()
                .appendEncodedPath(relativePosterPath)
                .build();
        return posterUri.toString();
    }

    public static String getApiKey() {
        return BuildConfig.THE_MOVIE_DB_API_KEY;
    }

    public static TheMovieDbService createService() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;

        return retrofit.create(theMovieDbServiceDefinition);
    }

    public static Call<MovieListResponse> getPopularMovies() {
        return createService().getPopularMovies(getApiKey());
    }

    public static Call<MovieListResponse> getTopRatedMovies() {
        return createService().getTopRatedMovies(getApiKey());
    }

    public static Call<ReviewListResponse> getReviewsForMovie(final String id) {
        return createService().getReviewsForMovie(id, getApiKey());
    }

    public static Call<TrailerListResponse> getTrailersForMovie(final String id) {
        return createService().getTrailersForMovie(id, getApiKey());
    }

}
