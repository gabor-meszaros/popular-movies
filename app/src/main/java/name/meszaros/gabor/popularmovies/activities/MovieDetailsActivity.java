package name.meszaros.gabor.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.meszaros.gabor.popularmovies.BuildConfig;
import name.meszaros.gabor.popularmovies.adapters.ReviewsAdapter;
import name.meszaros.gabor.popularmovies.models.Movie;
import name.meszaros.gabor.popularmovies.R;
import name.meszaros.gabor.popularmovies.models.Review;
import name.meszaros.gabor.popularmovies.models.ReviewListResponse;
import name.meszaros.gabor.popularmovies.network.TheMovieDbService;
import name.meszaros.gabor.popularmovies.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

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

            final RecyclerView reviewsRecyclerView =
                    (RecyclerView) findViewById(R.id.recycler_reviews);
            final ReviewsAdapter adapter = new ReviewsAdapter();
            reviewsRecyclerView.setAdapter(adapter);
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;
            final TheMovieDbService theMovieDbService = retrofit.create(theMovieDbServiceDefinition);
            final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;
            final Call<ReviewListResponse> call =
                    theMovieDbService.getReviewsForMovie(movie.getId(), apiKey);
            call.enqueue(new Callback<ReviewListResponse>() {
                             @Override
                             public void onResponse(final Call<ReviewListResponse> call,
                                                    final Response<ReviewListResponse> response) {
                                 if (response.isSuccessful()) {
                                     final List<Review> reviews = response.body().getReviews();
                                     adapter.setReviews(reviews.toArray(new Review[0]));
                                 }
                             }

                             @Override
                             public void onFailure(final Call<ReviewListResponse> call,
                                                   final Throwable t) {

                             }
                         }
            );
        }
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
}
