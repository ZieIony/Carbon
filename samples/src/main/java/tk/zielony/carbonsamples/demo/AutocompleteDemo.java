package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

import carbon.widget.AutoCompleteTextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-26.
 */
public class AutoCompleteDemo extends Activity {
    String[] language ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                  (this,android.R.layout.select_dialog_item,language);

        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        search.setAdapter(adapter);
    }
}
