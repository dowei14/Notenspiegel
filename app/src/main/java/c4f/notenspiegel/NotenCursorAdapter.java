package c4f.notenspiegel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import c4f.notenspiegel.daten.NotenspiegelContract.NotenEntry;

/**
 * Created by dswvi on 07-Oct-17.
 */

public class NotenCursorAdapter extends CursorAdapter {

    public NotenCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_noten_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.noten_list_fach_name);
        TextView notenTextView = (TextView) view.findViewById(R.id.noten_list_note);

        int nameColumnIndex = cursor.getColumnIndex(NotenEntry.COLUMN_FACH_NAME);
        int noteColumnIndex = cursor.getColumnIndex(NotenEntry.CLUMN_NOTE);

        String fachName = cursor.getString(nameColumnIndex);
        String notenName = Double.toString( cursor.getDouble(noteColumnIndex) / 100);

        nameTextView.setText(fachName);
        notenTextView.setText(notenName);
    }
}
