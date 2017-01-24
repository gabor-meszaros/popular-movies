package name.meszaros.gabor.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BrowseMoviesActivity extends AppCompatActivity implements FetchMoviesTask.Listener {

    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_movies);

        mMoviesRecyclerView = (RecyclerView) findViewById( R.id.rv_movies );

        mAdapter = new MoviesAdapter(null);

        mMoviesRecyclerView.setAdapter(mAdapter);

        final Context context = this;
        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);

        mMoviesRecyclerView.setLayoutManager(layoutManager);

        new FetchMoviesTask(this).execute();
    }

    @Override
    public void onFetchMoviesFinished(Movie[] movies) {
        mAdapter.setMoviesData(movies);
        // We don't need to notify the Adapter's listeners as the setMoviesData()
        // had already done that for us
    }
}
