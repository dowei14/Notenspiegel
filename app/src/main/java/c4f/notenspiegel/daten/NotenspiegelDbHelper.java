package c4f.notenspiegel.daten;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;
import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;

public class NotenspiegelDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "notenspiegel.db";
    private static final int DATABASE_VERSION = 1;

    public NotenspiegelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_NOTEN =  "CREATE TABLE " + NotenEntry.TABLE_NAME + " ("
                + NotenEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotenEntry.COLUMN_NOTE + " INTEGER NOT NULL, "
                + NotenEntry.COLUMN_NOTEN_NAME + " TEXT NOT NULL, "
                + NotenEntry.COLUMN_FACH_ID + " INTEGER NOT NULL, "
                + NotenEntry.COLUMN_GEWICHTUNG + " INTEGER NOT NULL );";

        db.execSQL(SQL_CREATE_TABLE_NOTEN);

        String SQL_CREATE_TABLE_FAECHER =  "CREATE TABLE " + FachEntry.TABLE_NAME + " ("
                + FachEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FachEntry.COLUMN_FACH_NAME + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE_FAECHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Datenbank noch auf dem aktuellen Stand
    }
}
