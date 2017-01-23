package name.meszaros.gabor.popularmovies;

/**
 * The Movie class represent the model of a movie.
 */
public class Movie {
    private String mTitle;

    public Movie(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
