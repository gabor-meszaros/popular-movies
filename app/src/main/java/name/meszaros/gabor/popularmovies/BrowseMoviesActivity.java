package name.meszaros.gabor.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class BrowseMoviesActivity extends AppCompatActivity implements FetchMoviesTask.Listener {

    private static final String LOG_TAG = BrowseMoviesActivity.class.getSimpleName();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_browse_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_switch_popular:
                Toast.makeText(this, "Switch popular selected.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_switch_highest_rated:
                Toast.makeText(this, "Switch highest rated selected.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.w(LOG_TAG, "Menu selection is not handled. ItemId: " + itemId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchMoviesFinished(Movie[] movies) {
        mAdapter.setMoviesData(movies);
        // We don't need to notify the Adapter's listeners as the setMoviesData()
        // had already done that for us
    }
}
