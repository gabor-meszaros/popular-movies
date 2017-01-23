package name.meszaros.gabor.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BrowseMoviesActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_movies);

        mMoviesRecyclerView = (RecyclerView) findViewById( R.id.rv_movies );

        final MoviesAdapter adapter = new MoviesAdapter();

        mMoviesRecyclerView.setAdapter(adapter);

        final Context context = this;
        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
    }
}
