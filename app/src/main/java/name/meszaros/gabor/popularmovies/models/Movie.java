package name.meszaros.gabor.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * The Movie class represent the model of a movie.
 */
public final class Movie implements Parcelable {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("id")
    private String mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("original_title")
    private String mOriginalTitle;

    @SerializedName("overview")
    private String mSynopsis;

    @SerializedName("vote_average")
    private String mUserRating;

    @SerializedName("release_date")
    private Date mReleaseDate;

    @SerializedName("poster_path")
    private String mPosterPath;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterLink() {
        return IMAGE_BASE_URL + mPosterPath;
    }

    /**
     * This function is used to deflate the POJO before sending it to the destination activity.
     * Keep your eyes on the order of the statements in this function and in the corresponding
     * constructor.
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mOriginalTitle);
        dest.writeString(mSynopsis);
        dest.writeString(mUserRating);
        dest.writeLong(mReleaseDate.getTime()); // Faster than using the Serializable interface
        dest.writeString(mPosterPath);
    }

    /**
     * The funcion is used to inflate the POJO once it has reached its destination activity.
     * Keep your eyes on the order of the statements in this constructor and in the corresponding
     * function.
     */
    public Movie(final Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mOriginalTitle = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readString();
        mReleaseDate = new Date(in.readLong()); // Faster than using the Serializable interface
        mPosterPath = in.readString();
    }

    /**
     * This function is needed by the interface. It does not harm if you just return 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * By not adding this static member will result an "android.os.BadParcelableException:
     * Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class" exception.
     */
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(final Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(final int size) {
            return new Movie[size];
        }
    };
}
