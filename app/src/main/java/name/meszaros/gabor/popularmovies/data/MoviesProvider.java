package name.meszaros.gabor.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;

public class MoviesProvider extends ContentProvider {

    private static final int MATCH_MOVIES = 100;

    private MoviesDbHelper mDatabaseHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MATCH_MOVIES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MATCH_MOVIES:
                cursor = mDatabaseHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH_MOVIES:
                database.insertOrThrow(MovieEntry.TABLE_NAME, null, values);
                notifyChange(uri);
                return MovieEntry.buildMovieUri(values.getAsString(MovieEntry.COLUMN_ID));
            default:
                throw  new UnsupportedOperationException("Unknown insert uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int numberOfRowsDeleted;

        if (null == selection) selection = "1";

        int match = sUriMatcher.match(uri);

        switch (match) {
            case MATCH_MOVIES:
                numberOfRowsDeleted = mDatabaseHelper.getWritableDatabase().delete(
                        MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown delete uri: " + uri);
        }

        if (0 != numberOfRowsDeleted) {
            notifyChange(uri);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    private void notifyChange(Uri uri) {
        final Context context = getContext();
        if (null != context) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }
}
