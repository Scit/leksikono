package ru.scit.Leksikono;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: scit
 * Date: 9/23/13
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuggestionTextView extends AutoCompleteTextView {
    ArrayAdapter<String> adapter;
    Context context;

    public SuggestionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item);
        this.setAdapter(adapter);
        this.setThreshold(1);
    }

    public void showSuggestions(List<String> suggestions) {
        adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item, suggestions);
        this.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        this.showDropDown();
    }

    public void clearSuggestions() {
        adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item);
        this.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
