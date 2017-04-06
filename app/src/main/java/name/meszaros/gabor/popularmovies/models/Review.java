package name.meszaros.gabor.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class Review implements Parcelable {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @SerializedName("url")
    private String mUrl;

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * This function is used to deflate the POJO before sending it to the destination activity.
     * Keep your eyes on the order of the statements in this function and in the corresponding
     * constructor.
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
    }

    /**
     * The funcion is used to inflate the POJO once it has reached its destination activity.
     * Keep your eyes on the order of the statements in this constructor and in the corresponding
     * function.
     */
    public Review(final Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
        mUrl = in.readString();
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

