package c4f.notenspiegel;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import c4f.notenspiegel.daten.NotenspiegelContract;
import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;

public class FaecherActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_NAME = FaecherActivity.class.getSimpleName();

    /** Identifier for the pet data loader */
    private static final int NOTENSPIEGEL_LOADER = 0;

    /** Adapter for the ListView */
    FachCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faecher);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_faecher);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaecherActivity.this, FachEditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView faecherListView = (ListView) findViewById(R.id.faecher_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.leerer_text_view_faecher);
        faecherListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new FachCursorAdapter(this, null);
        faecherListView.setAdapter(mCursorAdapter);
        // Setup the item click listener
        faecherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(FaecherActivity.this, NotenActivity.class);
                /*
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                int nameColumnIndex = cursor.getColumnIndex(FachEntry.COLUMN_FACH_NAME);
                String fachName = cursor.getString(nameColumnIndex);

                Uri fachUri = Uri.withAppendedPath(NotenspiegelContract.NotenEntry.CONTENT_URI, fachName);
                */
                Uri fachUri = ContentUris.withAppendedId(NotenspiegelContract.NotenEntry.CONTENT_FILTER_FACH_URI,id);
                // Set the URI on the data field of the intent
                intent.setData(fachUri);
                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(NOTENSPIEGEL_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faecher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_dummy_fach_einfuegen:
                fachEinfuegen();
                break;
            case R.id.action_alle_faecher_loeschen:
                alleFaecherLoeschen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                FachEntry._ID,
                FachEntry.COLUMN_FACH_NAME,
                FachEntry.COLUMN_DURCHSCHNITT_NAME};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                FachEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void fachEinfuegen() {
        ContentValues values = new ContentValues();
        values.put(FachEntry.COLUMN_FACH_NAME, "Mathe");

        Uri newUri = getContentResolver().insert(FachEntry.CONTENT_URI, values);
    }

    private void alleFaecherLoeschen() {
        int rowsDeleted = getContentResolver().delete(FachEntry.CONTENT_URI, null, null);
        int rowsNotenDeleted = getContentResolver().delete(NotenspiegelContract.NotenEntry.CONTENT_URI, null, null);
        Log.v(LOG_NAME, rowsDeleted + " rows deleted from pet database");
    }

}
