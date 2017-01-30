package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.AutoCompleteEditText;
import carbon.widget.AutoCompleteLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-26.
 */
public class AutoCompleteDemo extends Activity {

    String[] fruits = {"Strawberry\npie", "Apple\npie", "Orange\njuice", "Lemon\njuice", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        AutoCompleteLayout autoCompleteLayout = (AutoCompleteLayout) findViewById(R.id.autoComplete);
        autoCompleteLayout.setDataProvider(new AutoCompleteEditText.AutoCompleteDataProvider() {

            @Override
            public int getItemCount() {
                return fruits.length;
            }

            @Override
            public String getItem(int i) {
                return fruits[i];
            }

            @Override
            public String[] getItemWords(int i) {
                return fruits[i].split("\n");
            }
        });
    }
}
