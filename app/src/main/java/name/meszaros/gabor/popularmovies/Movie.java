package name.meszaros.gabor.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * The Movie class represent the model of a movie.
 */
public final class Movie implements Parcelable {

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

    /**
     * This function is used to deflate the POJO before sending it to the destination activity.
     * Keep your eyes on the order of the statements in this function and in the corresponding
     * constructor.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
    }

    /**
     * The funcion is used to inflate the POJO once it has reached its destination activity.
     * Keep your eyes on the order of the statements in this constructor and in the corresponding
     * function.
     */
    public Movie(Parcel in) {
        mTitle = in.readString();
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
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
