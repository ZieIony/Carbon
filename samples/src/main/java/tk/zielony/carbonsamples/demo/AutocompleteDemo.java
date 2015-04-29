package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import carbon.widget.AutoCompleteTextView;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-26.
 */
public class AutoCompleteDemo extends Activity {
    String[] language = {"C", "C++", "Java", ".NET", "iPhone", "Android", "ASP.NET", "PHP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(language);

        final View dropDown = findViewById(R.id.resultsDropDown);

        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        search.setAdapter(adapter);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dropDown.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });
        search.setOnAutoCompleteListener(new AutoCompleteTextView.OnAutoCompleteListener() {
            @Override
            public void onAutoComplete() {
                dropDown.requestLayout();
            }
        });

        RecyclerView recycler = (RecyclerView) findViewById(R.id.results);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search.setText(language[position]);
            }
        });
    }
}
