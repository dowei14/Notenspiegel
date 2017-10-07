package c4f.notenspiegel;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;

public class FaecherActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    /** Identifier for the pet data loader */
    private static final int NOTENSPIEGEL_LOADER = 0;

    /** Adapter for the ListView */
    FachCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_faecher);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //Intent intent = new Intent(FaecherActivity.this, NotenActivity.class);
                //startActivity(intent);
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();

            }
        });

        // Find the ListView which will be populated with the pet data
        ListView faecherListView = (ListView) findViewById(R.id.faecher_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.leerer_text_view);
        faecherListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new FachCursorAdapter(this, null);
        faecherListView.setAdapter(mCursorAdapter);
/**     TODO:

        // Setup the item click listener
        faecherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(FaecherActivity.this, NotenActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentFachUri = ContentUris.withAppendedId(FachEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentFachUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

 */
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
            case R.id.action_insert_dummy_data:
                // TODO:
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_delete_all_entries:
                // TODO:
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
