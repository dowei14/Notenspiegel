package c4f.notenspiegel.daten;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract Datei
 *
 * hier sind die Namen der Spalten der verschiedenen Datenbanken festgelegt, sowie der Pfaf
 * an dem die Datenbank erreicht werden kann.
 */

public final class NotenspiegelContract {
    /**
     * Leerer Constructor damit nicht jemand ausversehen die Klasse instantiert
     */
    private NotenspiegelContract(){}

    /**
     * Das ist der Name der des Content Providers - damit kann auf die Datenbank zugegriffen werden
     */
    public static final String CONTENT_AUTHORITY = "com.c4f.android.notenspiegel";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAECHER = "faecher";

    public static final String PATH_NOTEN = "noten";

    public static final class FachEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAECHER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAECHER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAECHER;

        public final static String TABLE_NAME = "faecher";


        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FACH_NAME ="name";
    }

    public static final class NotenEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTEN);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTEN;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTEN;

        public final static String TABLE_NAME = "noten";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FACH_NAME ="fach_name";

        public final static String COLUMN_NOTEN_NAME ="noten_name";

        public final static String CLUMN_NOTE ="note";
    }

}
