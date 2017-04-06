package name.meszaros.gabor.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The model of themoviedb.org's response for the /movie/{id}/reviews query.
 * For the simplicity it only contains the fields those are interesting for us.
 */
public final class ReviewListResponse {

    @SerializedName("results")
    private List<Review> mReviews = new ArrayList<>();

    public List<Review> getReviews() {
        return mReviews;
    }
}

