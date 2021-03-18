package tk.zielony.carbonsamples.demo;

import android.os.Bundle;

import carbon.widget.ArraySearchAdapter;
import carbon.widget.AutoCompleteEditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_autocomplete, titleId = R.string.autoCompleteActivity_title)
public class AutoCompleteActivity extends ThemedActivity {

    String[] fruits = {"Strawberry\npie", "Apple\npie", "Orange\njuice", "Lemon\njuice", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        AutoCompleteEditText autoCompleteLayout = findViewById(R.id.autoComplete);
        autoCompleteLayout.setDataProvider(new ArraySearchAdapter<String>(fruits) {

            @Override
            public String[] getItemWords(String item) {
                return item.split("\n");
            }
        });
    }
}
