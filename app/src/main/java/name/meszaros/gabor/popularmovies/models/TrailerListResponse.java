package name.meszaros.gabor.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * The model of themoviedb.org's response for the /movie/{id}/trailers query.
 * For the simplicity it only contains the fields those are interesting for us.
 */
public final class TrailerListResponse {

    @SerializedName("youtube")
    private List<Trailer> mTrailers = new ArrayList<>();

    public List<Trailer> getTrailers() {
        return mTrailers;
    }
}

