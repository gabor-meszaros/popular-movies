package name.meszaros.gabor.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import name.meszaros.gabor.popularmovies.data.MoviesContract.MovieEntry;

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieEntry.COLUMN_ID             + " TEXT NOT NULL, "                     +
                        MovieEntry.COLUMN_TITLE          + " TEXT NOT NULL, "                     +
                        MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "                     +
                        MovieEntry.COLUMN_SYNOPSIS       + " TEXT NOT NULL, "                     +
                        MovieEntry.COLUMN_USER_RATING    + " TEXT NOT NULL, "                     +
                        MovieEntry.COLUMN_RELEASE_DATE   + " INTEGER NOT NULL, "                  +
                        MovieEntry.COLUMN_POSTER_PATH    + " TEXT NOT NULL "                      +

                        ");";

        database.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase database, final int oldVersion,
                          final int newVersion) {
        // There is no new version of the DB so no need to define Upgrade logic yet
    }
}
