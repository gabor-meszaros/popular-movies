package name.meszaros.gabor.popularmovies;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String INTENT_DATA = Movie.class.getName();

    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private ImageView mPosterImageView;
    private TextView mSimpleTitleTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mTitleTextView = (TextView) findViewById(R.id.text_movie_title);
        mOriginalTitleTextView = (TextView) findViewById(R.id.text_movie_original_title);
        mPosterImageView = (ImageView) findViewById(R.id.image_movie_poster);
        mSimpleTitleTextView = (TextView) findViewById(R.id.text_movie_simple_title);
        mRatingTextView = (TextView) findViewById(R.id.text_movie_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.text_movie_release_date);
        mSynopsisTextView = (TextView) findViewById(R.id.text_movie_synopsis);

        final Intent intent = getIntent();
        if (null != intent && intent.hasExtra(INTENT_DATA)) {
            final Movie movie = (Movie) intent.getParcelableExtra(INTENT_DATA);
            mTitleTextView.setText(movie.getTitle());
            mOriginalTitleTextView.setText("(" + movie.getOriginalTitle() + ")");

            final String posterLink = movie.getPosterLink();
            Picasso.with(this).load(posterLink).into(mPosterImageView);

            mSimpleTitleTextView.setText(movie.getTitle());
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
            mReleaseDateTextView.setText("Release date: " + dateFormat.format(movie.getReleaseDate()));
            mRatingTextView.setText("User rating: " + movie.getUserRating());
            mSynopsisTextView.setText(movie.getSynopsis());
        }
    }
}
