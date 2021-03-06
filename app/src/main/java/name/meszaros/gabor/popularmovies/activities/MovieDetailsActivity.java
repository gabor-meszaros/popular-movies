package name.meszaros.gabor.popularmovies.activities;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;
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

    @BindView(R.id.button_favorite)
    Button mFavoriteButton;

    @BindView(R.id.text_movie_synopsis)
    TextView mSynopsisTextView;

    @BindView(R.id.text_trailers_label)
    TextView mTrailersLabelTextView;

    @BindView(R.id.recycler_trailers)
    RecyclerView mTrailersRecyclerView;

    @BindView(R.id.text_reviews_label)
    TextView mReviewsLabelTextView;

    @BindView(R.id.recycler_reviews)
    RecyclerView mReviewsRecyclerView;

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
            final Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);

            mTitleTextView.setText(movie.getTitle());
            mOriginalTitleTextView.setText(getString(R.string.label_original_title,
                    movie.getOriginalTitle()));

            final String posterPath = movie.getPosterPath();
            Picasso.with(this).load(posterPath).into(mPosterImageView);

            mSimpleTitleTextView.setText(movie.getTitle());
            mReleaseDateTextView.setText(formatReleaseDateText(movie.getReleaseDate()));
            mRatingTextView.setText(getString(R.string.label_user_rating, movie.getUserRating()));
            mSynopsisTextView.setText(movie.getSynopsis());

            final String movieId = movie.getId();

            mReviewsAdapter = new ReviewsAdapter();
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            loadReviews(movieId);

            mTrailersAdapter = new TrailersAdapter(this);
            mTrailersRecyclerView.setAdapter(mTrailersAdapter);
            loadTrailers(movieId);

            setFavoriteButtonText(movieId);
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
                             hideDynamicContent();
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
                             hideDynamicContent();
                         }
                     }
        );
    }

    private void setFavoriteButtonText(final String movieId) {
        final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

                final boolean favoriteMovie = (null != cursor && cursor.getCount() != 0);

                if (favoriteMovie) {
                    mFavoriteButton.setText(getString(R.string.button_remove_from_favorites));
                } else {
                    mFavoriteButton.setText(getString(R.string.button_add_to_favorites));
                }

                if (null != cursor) cursor.close();
            }
        };

        final int anyId = 42; // We will not use it in the result handler function
        asyncQueryHandler.startQuery(anyId,
                null,
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.COLUMN_ID},
                MovieEntry.COLUMN_ID + "=?",
                new String[]{movieId},
                null);
    }

    private void hideDynamicContent() {
        mReviewsLabelTextView.setVisibility(View.GONE);
        mReviewsRecyclerView.setVisibility(View.GONE);
        mTrailersLabelTextView.setVisibility(View.GONE);
        mTrailersRecyclerView.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_share:
                shareMovie();
                break;
            default:
                Log.w(LOG_TAG, "Menu selection is not handled. ItemId: " + itemId);
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        final String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setText(createShareMovieIntentText())
                .startChooser();
    }

    private String createShareMovieIntentText() {
        final String movieTitle = mTitleTextView.getText().toString();
        final boolean movieHasAtLeastOneTrailer = (0 != mTrailersAdapter.getItemCount());
        if (movieHasAtLeastOneTrailer) {
            final Trailer[] trailers = mTrailersAdapter.getTrailers();
            final Trailer firstTrailer = trailers[0];
            final String firstTrailerLink = firstTrailer.getLink();

            return getString(R.string.share_movie_text_with_trailer, movieTitle, firstTrailerLink);
        } else {
            return getString(R.string.share_movie_text, movieTitle);
        }
    }

    public void onClickFavoritesButton(final View view) {
        final Button favoritesButton = (Button) view;
        final String favoritesButtonText = favoritesButton.getText().toString();
        final Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if (favoritesButtonText.equals(getString(R.string.button_add_to_favorites))) {
            final ContentValues values = getContentValuesFromMovie(movie);

            final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
                @Override
                protected void onInsertComplete(int token, Object cookie, Uri uri) {
                    favoritesButton.setText(getString(R.string.button_remove_from_favorites));
                }
            };
            final int anyId = 42; // We will not use it in the result handler function
            asyncQueryHandler.startInsert(anyId, null, MovieEntry.CONTENT_URI, values);

        } else {
            final AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
                @Override
                protected void onDeleteComplete(int token, Object cookie, int result) {
                    favoritesButton.setText(getString(R.string.button_add_to_favorites));
                }
            };
            final int anyId = 42; // We will not use it in the result handler function
            asyncQueryHandler.startDelete(anyId, null, MovieEntry.CONTENT_URI,
                    MovieEntry.COLUMN_ID + "=?", new String[]{movie.getId()});
        }
    }

    private ContentValues getContentValuesFromMovie(final Movie movie) {
        final ContentValues values = new ContentValues();

        values.put(MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        values.put(MovieEntry.COLUMN_USER_RATING, movie.getUserRating());

        final String releaseDate = String.valueOf(movie.getReleaseDate().getTime());
        values.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);

        values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        return values;
    }

    private String formatReleaseDateText(final Date releaseDate) {
        final String formattedReleaseDate = UiUtils.formatReleaseDate(releaseDate);
        return getString(R.string.label_release_date, formattedReleaseDate);
    }

    @Override
    public void onTrailerItemClick(final Trailer trailer) {
        final Uri trailerUri = Uri.parse(trailer.getLink());
        final Intent viewTrailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (null != viewTrailerIntent.resolveActivity(getPackageManager())) {
            startActivity(viewTrailerIntent);
        } else {
            Log.w(LOG_TAG, "No video player app found on the device.");
            Toast.makeText(this, getString(R.string.error_no_video_player), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
