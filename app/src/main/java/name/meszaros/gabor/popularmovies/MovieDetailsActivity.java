package name.meszaros.gabor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String INTENT_DATA = Movie.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        final TextView titleTextView = (TextView) findViewById(R.id.text_movie_title);

        final Intent intent = getIntent();
        if (null != intent && intent.hasExtra(INTENT_DATA)) {
            final Movie movie = (Movie) intent.getParcelableExtra(INTENT_DATA);
            titleTextView.setText(movie.getTitle());
        }
    }
}
