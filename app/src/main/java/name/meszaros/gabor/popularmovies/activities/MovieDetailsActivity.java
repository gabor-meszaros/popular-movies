package name.meszaros.gabor.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.utils.UiUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = Movie.class.getName();

    private static final double MOVIE_POSTER_WIDTH_HEIGHT_RATIO = 40.0 / 27.0;
    private static final double SCREEN_WIDTH_POSTER_WIDTH_RATIO = 0.4;
    private static final String USER_RATING_TEXT_PREFIX = "User rating: ";
    private static final String RELEASE_DATE_TEXT_PREFIX = "Release date: ";

    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private ImageView mPosterImageView;
    private TextView mSimpleTitleTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initializeViews();

        final Intent intent = getIntent();
        if (null != intent && intent.hasExtra(EXTRA_MOVIE)) {
            final Movie movie = (Movie) intent.getParcelableExtra(EXTRA_MOVIE);

            mTitleTextView.setText(movie.getTitle());
            mOriginalTitleTextView.setText(formatOriginalTitleText(movie.getOriginalTitle()));

            final String posterLink = movie.getPosterLink();
            Picasso.with(this).load(posterLink).into(mPosterImageView);

            mSimpleTitleTextView.setText(movie.getTitle());
            mReleaseDateTextView.setText(formatReleaseDateText(movie.getReleaseDate()));
            mRatingTextView.setText(formatUserRatingText(movie.getUserRating()));
            mSynopsisTextView.setText(movie.getSynopsis());
        }
    }

    private void initializeViews() {
        mTitleTextView = (TextView) findViewById(R.id.text_movie_title);
        mOriginalTitleTextView = (TextView) findViewById(R.id.text_movie_original_title);
        initializePosterImageView();
        mSimpleTitleTextView = (TextView) findViewById(R.id.text_movie_simple_title);
        mRatingTextView = (TextView) findViewById(R.id.text_movie_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.text_movie_release_date);
        mSynopsisTextView = (TextView) findViewById(R.id.text_movie_synopsis);
    }

    private void initializePosterImageView() {
        mPosterImageView = (ImageView) findViewById(R.id.image_movie_poster);

        final int posterWidth = calculatePosterWidthInPixel();
        final int posterHeight = calculatePosterHeightInPixel(posterWidth);

        setImageViewDimensionInPixels(mPosterImageView, posterWidth, posterHeight);
    }

    private void setImageViewDimensionInPixels(final ImageView view, final int width,
                                               final int height) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.requestLayout();
    }

    private int calculatePosterHeightInPixel(final int posterWidth) {
        return (int) (posterWidth * MOVIE_POSTER_WIDTH_HEIGHT_RATIO);
    }

    private int calculatePosterWidthInPixel() {
        final int displayWidth = getDisplayWidthInPixels();
        return (int) (displayWidth * SCREEN_WIDTH_POSTER_WIDTH_RATIO);
    }

    private int getDisplayWidthInPixels() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private static String formatOriginalTitleText(final String originalTitle) {
        return "(" + originalTitle + ")";
    }

    private String formatReleaseDateText(final Date releaseDate) {
        final String formattedReleaseDate = UiUtils.formatReleaseDate(releaseDate);
        final String releaseDateTextField = RELEASE_DATE_TEXT_PREFIX + formattedReleaseDate;
        return releaseDateTextField;
    }

    private String formatUserRatingText(final String userRating) {
        final String userRatingText = USER_RATING_TEXT_PREFIX + userRating;
        return userRatingText;
    }
}
