package c4f.notenspiegel;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import c4f.notenspiegel.daten.NotenspiegelContract;
import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;
import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;

public class NotenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_NAME = NotenActivity.class.getSimpleName();

    /** Identifier for the pet data loader */
    private static final int NOTENSPIEGEL_LOADER = 0;

    /** Adapter for the ListView */
    NotenCursorAdapter mCursorAdapter;

    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri mCurrentUri;

    private int mCurrentFachID;
    private String mCurrentFachName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noten);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_noten);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotenActivity.this, NotenEditorActivity.class);
                intent.putExtra(String.valueOf(R.string.passCurrentSubjectID),mCurrentFachID);
                intent.putExtra(String.valueOf(R.string.passCurrentSubjectName),mCurrentFachName);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView notenListView = (ListView) findViewById(R.id.noten_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.leerer_text_view_noten);
        notenListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new NotenCursorAdapter(this, null);
        notenListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        notenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(NotenActivity.this, NotenEditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentNotenUri = ContentUris.withAppendedId(NotenEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentNotenUri);
                intent.putExtra(String.valueOf(R.string.passCurrentSubjectID),mCurrentFachID);
                intent.putExtra(String.valueOf(R.string.passCurrentSubjectName),mCurrentFachName);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentUri = intent.getData();


        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.

        if (mCurrentUri != null) {
            getSupportLoaderManager().initLoader(NOTENSPIEGEL_LOADER, null, this);
        }
        updateSubjectAndTitle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_fach_bearbeiten:
                fachBearbeiten();
                break;
            case R.id.action_dummy_note_einfuegen:
                noteEinfuegen();
                break;
            case R.id.action_alle_noten_loeschen:
                alleNotenLoeschen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                NotenEntry._ID,
                NotenEntry.COLUMN_FACH_ID,
                NotenEntry.COLUMN_NOTEN_NAME,
                NotenEntry.COLUMN_NOTE,
                NotenEntry.COLUMN_GEWICHTUNG};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void noteEinfuegen() {
        ContentValues values = new ContentValues();
        values.put(NotenEntry.COLUMN_FACH_ID, mCurrentFachID);
        values.put(NotenEntry.COLUMN_NOTEN_NAME, "Klassenarbeit 1");
        values.put(NotenEntry.COLUMN_NOTE, "150");
        values.put(NotenEntry.COLUMN_GEWICHTUNG, "100");

        Uri newUri = getContentResolver().insert(NotenEntry.CONTENT_URI, values);
    }

    private void alleNotenLoeschen() {
        int rowsDeleted = getContentResolver().delete(NotenEntry.CONTENT_URI, null, null);
        Log.v(LOG_NAME, rowsDeleted + " rows deleted from pet database");
    }

    private String getFachName(int id){
        String fachName = null;
        Uri fachUri = ContentUris.withAppendedId(NotenspiegelContract.FachEntry.CONTENT_URI,id);
        String[] projection = {
                NotenspiegelContract.FachEntry._ID,
                NotenspiegelContract.FachEntry.COLUMN_FACH_NAME };

        Cursor cursor = getContentResolver().query(fachUri,projection,null,null,null);

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(FachEntry.COLUMN_FACH_NAME);
            fachName = cursor.getString(nameColumnIndex);
        }

        return fachName;
    }

    private void fachBearbeiten(){
        Intent intent = new Intent(NotenActivity.this, FachEditorActivity.class);
        Uri fachUri = ContentUris.withAppendedId(FachEntry.CONTENT_URI,mCurrentFachID);
        // Set the URI on the data field of the intent
        intent.setData(fachUri);
        startActivity(intent);
    }

    private void updateSubjectAndTitle(){
        if (mCurrentUri != null) {
            mCurrentFachID = Integer.parseInt(mCurrentUri.getLastPathSegment());
            mCurrentFachName = getFachName(mCurrentFachID);
            setTitle(mCurrentFachName);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateSubjectAndTitle();
        getSupportLoaderManager().restartLoader(NOTENSPIEGEL_LOADER,null, this);
    }
}
