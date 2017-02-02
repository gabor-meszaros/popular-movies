package name.meszaros.gabor.popularmovies.network;

import name.meszaros.gabor.popularmovies.models.MovieListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The Movie DB service definition for retrofit.
 */
public interface TheMovieDbService {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    @GET("movie/popular/")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated/")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String apiKey);
}
