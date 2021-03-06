package name.meszaros.gabor.popularmovies.utils;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Helper class containing the static methods which are used during the UI building (e.g. text
 * manipulation, formatting, etc.).
 */
public final class UiUtils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd MMM yyyy");

    public static String formatReleaseDate(final Date date) {
        return DATE_FORMATTER.format(date);
    }
}
