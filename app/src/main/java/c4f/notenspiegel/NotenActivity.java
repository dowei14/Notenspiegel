package c4f.notenspiegel;

import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.Toast;

import c4f.notenspiegel.daten.NotenspiegelContract;
import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;

public class NotenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_NAME = NotenActivity.class.getSimpleName();

    /** Identifier for the pet data loader */
    private static final int NOTENSPIEGEL_LOADER = 0;

    /** Adapter for the ListView */
    NotenCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noten);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_noten);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: NotenBearbeitenActivity erstellen und hier verlinken
                //Intent intent = new Intent(NotenActivity.this, NotenBearbeitenActivity.class);
                //startActivity(intent);
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();

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
/**     TODO: bei Onclicklistener Note bearbeiten

        // Setup the item click listener
        notenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(NotenActivity.this, NoteBearbeiten.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentNotenUri = ContentUris.withAppendedId(NotenEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentNotenUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

 */
        getSupportLoaderManager().initLoader(NOTENSPIEGEL_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
                NotenEntry.COLUMN_FACH_NAME,
                NotenEntry.CLUMN_NOTE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                NotenEntry.CONTENT_URI,   // Provider content URI to query
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
        values.put(NotenEntry.COLUMN_FACH_NAME, "Mathe");
        values.put(NotenEntry.CLUMN_NOTE, "150");

        Uri newUri = getContentResolver().insert(NotenEntry.CONTENT_URI, values);
    }

    private void alleNotenLoeschen() {
        int rowsDeleted = getContentResolver().delete(NotenEntry.CONTENT_URI, null, null);
        Log.v(LOG_NAME, rowsDeleted + " rows deleted from pet database");
    }

}
