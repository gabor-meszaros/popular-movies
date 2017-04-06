package name.meszaros.gabor.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class Trailer implements Parcelable {

    private static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_THUMBNAIL_URL_PATTERN =
            "https://img.youtube.com/vi/%s/mqdefault.jpg";

    @SerializedName("name")
    private String mName;

    @SerializedName("source")
    private String mId;

    @SerializedName("type")
    private String mType;

    public String getName() {
        return mName;
    }

    public String getLink() {
        return YOUTUBE_VIDEO_BASE_URL + mId;
    }

    public String getThumbnailLink() {
        return String.format(YOUTUBE_THUMBNAIL_URL_PATTERN, mId);
    }

    public String getType() {
        return mType;
    }

    /**
     * This function is used to deflate the POJO before sending it to the destination activity.
     * Keep your eyes on the order of the statements in this function and in the corresponding
     * constructor.
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mName);
        dest.writeString(mId);
        dest.writeString(mType);
    }

    /**
     * The funcion is used to inflate the POJO once it has reached its destination activity.
     * Keep your eyes on the order of the statements in this constructor and in the corresponding
     * function.
     */
    public Trailer(final Parcel in) {
        mName = in.readString();
        mId = in.readString();
        mType = in.readString();
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

