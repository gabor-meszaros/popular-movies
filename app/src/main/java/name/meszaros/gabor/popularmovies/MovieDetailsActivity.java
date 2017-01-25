package name.meszaros.gabor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        final TextView titleTextView = (TextView) findViewById(R.id.tv_movie_title);
        final Intent intent = getIntent();
        if (null != intent && intent.hasExtra(Intent.EXTRA_TEXT)) {
            titleTextView.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}
