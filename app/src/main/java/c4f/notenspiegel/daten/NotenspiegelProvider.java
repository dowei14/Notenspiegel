package c4f.notenspiegel.daten;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;
import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;


public class NotenspiegelProvider extends ContentProvider{
    public static final String LOG_TAG = NotenspiegelProvider.class.getSimpleName();
    private static final int NOTEN = 100;
    private static final int NOTEN_ID = 101;
    private static final int NOTEN_FACH_FILTER = 102;
    private static final int FACH = 200;
    private static final int FACH_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(NotenspiegelContract.CONTENT_AUTHORITY, NotenspiegelContract.PATH_NOTEN, NOTEN);
        sUriMatcher.addURI(NotenspiegelContract.CONTENT_AUTHORITY, NotenspiegelContract.PATH_NOTEN + "/#", NOTEN_ID);
        sUriMatcher.addURI(NotenspiegelContract.CONTENT_AUTHORITY, NotenspiegelContract.PATH_NOTEN_FACH + "/*", NOTEN_FACH_FILTER);
        sUriMatcher.addURI(NotenspiegelContract.CONTENT_AUTHORITY, NotenspiegelContract.PATH_FAECHER, FACH);
        sUriMatcher.addURI(NotenspiegelContract.CONTENT_AUTHORITY, NotenspiegelContract.PATH_FAECHER + "/#", FACH_ID);
    }

    private NotenspiegelDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new NotenspiegelDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FACH:
                cursor = database.query(FachEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FACH_ID:
                selection = FachEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(FachEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NOTEN:
                cursor = database.query(NotenEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NOTEN_ID:
                selection = NotenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(NotenEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NOTEN_FACH_FILTER:
                selection = NotenEntry.COLUMN_FACH_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };

                cursor = database.query(NotenEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTEN:
                return NotenEntry.CONTENT_LIST_TYPE;
            case NOTEN_ID:
                return NotenEntry.CONTENT_ITEM_TYPE;
            case NOTEN_FACH_FILTER:
                return NotenEntry.CONTENT_LIST_TYPE;
            case FACH:
                return FachEntry.CONTENT_LIST_TYPE;
            case FACH_ID:
                return FachEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FACH:
                return insertFach(uri, contentValues);
            case NOTEN:
                return insertNote(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertNote(Uri uri, ContentValues values){
        String name = values.getAsString(NotenEntry.COLUMN_FACH_ID);
        if (name == null) {
            throw new IllegalArgumentException("Fachname muss eingetragen werden");
        }

        String notenName = values.getAsString(NotenEntry.COLUMN_NOTEN_NAME);
        if (notenName == null) {
            throw new IllegalArgumentException("NotenName muss eingetragen werden");
        }

        Integer note = values.getAsInteger(NotenEntry.CLUMN_NOTE);
        if (note == null ) {
            throw new IllegalArgumentException("Note muss eingetragen werden");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(NotenEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Reihe konnte nicht bei folgender URI eingefuegt werden " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertFach(Uri uri, ContentValues values){
        String name = values.getAsString(FachEntry.COLUMN_FACH_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Fachname muss eingetragen werden");
        }
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(FachEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Reihe konnte nicht bei folgender URI eingefuegt werden " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTEN:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(NotenEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTEN_ID:
                // Delete a single row given by the ID in the URI
                selection = NotenEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(NotenEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FACH:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(FachEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FACH_ID:
                // Delete a single row given by the ID in the URI
                selection = FachEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(FachEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTEN:
                return updateNote(uri, contentValues, selection, selectionArgs);
            case NOTEN_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = NotenEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, contentValues, selection, selectionArgs);
            case NOTEN_FACH_FILTER:
                selection = NotenEntry.COLUMN_FACH_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, contentValues, selection, selectionArgs);
            case FACH:
                return updateFach(uri, contentValues, selection, selectionArgs);
            case FACH_ID:
                selection = FachEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateFach(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update ist nicht unterstützt für " + uri);
        }
    }
    private int updateNote(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(NotenEntry.COLUMN_FACH_ID)) {
            String name = values.getAsString(NotenEntry.COLUMN_FACH_ID);
            if (name == null) {
                throw new IllegalArgumentException("Fachname benötigt");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(NotenEntry.CLUMN_NOTE)) {
            Integer gender = values.getAsInteger(NotenEntry.CLUMN_NOTE);
            if (gender == null) {
                throw new IllegalArgumentException("Note muss eingetragen werden");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(NotenEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
    private int updateFach(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(FachEntry.COLUMN_FACH_NAME)) {
            String name = values.getAsString(FachEntry.COLUMN_FACH_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Fachname benötigt");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(FachEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
