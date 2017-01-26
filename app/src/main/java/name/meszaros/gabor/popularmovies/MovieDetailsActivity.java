package name.meszaros.gabor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String INTENT_DATA = Movie.class.getName();

    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private ImageView mPoster;
    private TextView mSimpleTitleTextView;
    private TextView mRating;
    private TextView mReleaseDateTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mTitleTextView = (TextView) findViewById(R.id.text_movie_title);
        mOriginalTitleTextView = (TextView) findViewById(R.id.text_movie_original_title);
        mPoster = (ImageView) findViewById(R.id.image_movie_poster);
        mSimpleTitleTextView = (TextView) findViewById(R.id.text_movie_simple_title);
        mRating = (TextView) findViewById(R.id.text_movie_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.text_movie_release_date);
        mSynopsisTextView = (TextView) findViewById(R.id.text_movie_synopsis);

        final Intent intent = getIntent();
        if (null != intent && intent.hasExtra(INTENT_DATA)) {
            final Movie movie = (Movie) intent.getParcelableExtra(INTENT_DATA);
            mTitleTextView.setText(movie.getTitle());
            mOriginalTitleTextView.setText("(" + movie.getOriginalTitle() + ")");

            final String posterLink = movie.getPosterLink();
            Picasso.with(this).load(posterLink).into(mPoster);

            mSimpleTitleTextView.setText(movie.getTitle());
            mRating.setText("User rating: " + movie.getUserRating());
            mReleaseDateTextView.setText("Release date: " + movie.getReleaseDate().toString());
            mSynopsisTextView.setText(movie.getSynopsis());
        }
    }
}
