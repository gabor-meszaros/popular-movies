package name.meszaros.gabor.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The model of themoviedb.org's response for the popular movies and the top rated movies
 * queries. For the simplicity it only contains the fields those are interesting for us.
 */
public final class MovieListResponse {

    @SerializedName("results")
    private List<Movie> mMovies = new ArrayList<>();

    public List<Movie> getMovies() {
        return mMovies;
    }
}
