package c4f.notenspiegel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import c4f.notenspiegel.daten.NotenspiegelContract.FachEntry;
import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;

/**
 * Created by dswvi on 07-Oct-17.
 */

public class FachCursorAdapter extends CursorAdapter {

    public FachCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_faecher_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.fach_name);

        int nameColumnIndex = cursor.getColumnIndex(FachEntry.COLUMN_FACH_NAME);

        String fachName = cursor.getString(nameColumnIndex);

        nameTextView.setText(fachName);
    }
}
