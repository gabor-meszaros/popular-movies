package name.meszaros.gabor.popularmovies;

import com.google.gson.annotations.SerializedName;

/**
 * The Movie class represent the model of a movie.
 */
public final class Movie {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    public Movie(String title, String posterPath) {
        this.mTitle = title;
        this.mPosterPath = posterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterLink() {
        return IMAGE_BASE_URL + mPosterPath;
    }
}
