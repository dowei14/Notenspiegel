package c4f.notenspiegel;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import c4f.notenspiegel.daten.NotenspiegelContract;

/**
 * Created by dswvi on 12-Oct-17.
 */

public final class DbUtils {
    private static final String LOG_TAG = DbUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link DbUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DbUtils (and an object instance of DbUtils is not needed).
     */
    private DbUtils(){}

    public static Double getDurchschnitt(Uri uri, ContentResolver resolver){
        Double durchschnitt = 0.0;
        if (uri != null){
            double count = 0;
            String[] projection = {
                    NotenspiegelContract.NotenEntry._ID,
                    NotenspiegelContract.NotenEntry.COLUMN_GEWICHTUNG,
                    NotenspiegelContract.NotenEntry.COLUMN_NOTE};

            Cursor cursor = resolver.query(uri,projection,null,null,null);
            while (cursor.moveToNext()) {
                int gewichtungIndex = cursor.getColumnIndex(NotenspiegelContract.NotenEntry.COLUMN_GEWICHTUNG);
                int noteIndex = cursor.getColumnIndex(NotenspiegelContract.NotenEntry.COLUMN_NOTE);
                durchschnitt += cursor.getDouble(noteIndex) / 100 * cursor.getDouble(gewichtungIndex) / 100;
                count += cursor.getDouble(gewichtungIndex) / 100;
            }
            if (count != 0) durchschnitt = durchschnitt / count;
        }
        return durchschnitt;
    }



}
