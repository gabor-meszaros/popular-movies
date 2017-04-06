package name.meszaros.gabor.popularmovies.network;

import name.meszaros.gabor.popularmovies.models.MovieListResponse;
import name.meszaros.gabor.popularmovies.models.ReviewListResponse;
import name.meszaros.gabor.popularmovies.models.TrailerListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("movie/{id}/reviews")
    Call<ReviewListResponse> getReviewsForMovie(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/trailers")
    Call<TrailerListResponse>  getTrailersForMovie(@Path("id") String id, @Query("api_key") String apiKey);
}
