package name.meszaros.gabor.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The Movie DB service definition for retrofit.
 */
public interface TheMovieDbService {

    @GET("movie/popular/")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey);

}
