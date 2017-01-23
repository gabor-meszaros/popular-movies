package name.meszaros.gabor.popularmovies;

/**
 * The Movie class represent the model of a movie.
 */
public class Movie {
    private String mTitle;
    private String mPosterLink;

    public Movie(String title, String posterLink) {
        mTitle = title;
        mPosterLink = posterLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterLink() {
        return mPosterLink;
    }
}
