package name.meszaros.gabor.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.meszaros.gabor.popularmovies.adapters.ReviewsAdapter;
import name.meszaros.gabor.popularmovies.adapters.TrailersAdapter;
import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.models.Review;
import name.meszaros.gabor.popularmovies.models.ReviewListResponse;
import name.meszaros.gabor.popularmovies.models.Trailer;
import name.meszaros.gabor.popularmovies.models.TrailerListResponse;
import name.meszaros.gabor.popularmovies.utils.TheMovieDbUtils;
import name.meszaros.gabor.popularmovies.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity
        implements TrailersAdapter.OnClickListener {
    private final static String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE = Movie.class.getName();

    private static final double MOVIE_POSTER_WIDTH_HEIGHT_RATIO = 40.0 / 27.0;
    private static final double SCREEN_WIDTH_POSTER_WIDTH_RATIO = 0.4;
    private static final String USER_RATING_TEXT_PREFIX = "User rating: ";
    private static final String RELEASE_DATE_TEXT_PREFIX = "Release date: ";

    @BindView(R.id.text_movie_title)
    TextView mTitleTextView;

    @BindView(R.id.text_movie_original_title)
    TextView mOriginalTitleTextView;

    @BindView(R.id.image_movie_poster)
    ImageView mPosterImageView;

    @BindView(R.id.text_movie_simple_title)
    TextView mSimpleTitleTextView;

    @BindView(R.id.text_movie_rating)
    TextView mRatingTextView;

    @BindView(R.id.text_movie_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.text_movie_synopsis)
    TextView mSynopsisTextView;

    private ReviewsAdapter mReviewsAdapter;
    private TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        initializePosterImageView();

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

            mReviewsAdapter = new ReviewsAdapter();
            final RecyclerView reviewsRecyclerView =
                    (RecyclerView) findViewById(R.id.recycler_reviews);
            reviewsRecyclerView.setAdapter(mReviewsAdapter);

            loadReviews(movie.getId());

            mTrailersAdapter = new TrailersAdapter(this);
            final RecyclerView trailersRecyclerView =
                    (RecyclerView) findViewById(R.id.recycler_trailers);
            trailersRecyclerView.setAdapter(mTrailersAdapter);

            loadTrailers(movie.getId());
        }
    }

    private void loadReviews(final String movieId) {
        final Call<ReviewListResponse> call = TheMovieDbUtils.getReviewsForMovie(movieId);
        call.enqueue(new Callback<ReviewListResponse>() {
                         @Override
                         public void onResponse(final Call<ReviewListResponse> call,
                                                final Response<ReviewListResponse> response) {
                             if (response.isSuccessful()) {
                                 final List<Review> reviews = response.body().getReviews();
                                 mReviewsAdapter.setReviews(reviews.toArray(new Review[0]));
                             }
                         }

                         @Override
                         public void onFailure(final Call<ReviewListResponse> call,
                                               final Throwable t) {

                         }
                     }
        );
    }

    private void loadTrailers(final String movieId) {
        final Call<TrailerListResponse> call = TheMovieDbUtils.getTrailersForMovie(movieId);
        call.enqueue(new Callback<TrailerListResponse>() {
                         @Override
                         public void onResponse(final Call<TrailerListResponse> call,
                                                final Response<TrailerListResponse> response) {
                             if (response.isSuccessful()) {
                                 final List<Trailer> trailers = response.body().getTrailers();
                                 mTrailersAdapter.setTrailers(trailers.toArray(new Trailer[0]));
                             }
                         }

                         @Override
                         public void onFailure(final Call<TrailerListResponse> call,
                                               final Throwable t) {

                         }
                     }
        );
    }

    private void initializePosterImageView() {
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

    @Override
    public void onTrailerItemClick(final Trailer trailer) {
        final Uri trailerUri = Uri.parse(trailer.getLink());
        final Intent viewTrailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (null != viewTrailerIntent.resolveActivity(getPackageManager())) {
            startActivity(viewTrailerIntent);
        } else {
            Log.w(LOG_TAG, "No video player app found on the device.");
            Toast.makeText(this, "Cannot find video player app.", Toast.LENGTH_SHORT).show();
        }
    }
}
