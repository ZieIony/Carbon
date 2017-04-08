package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.widget.ExpandableRecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class ExpandableRecyclerActivity extends SamplesActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablerecycler);

        Samples.initToolbar(this, getString(R.string.expandableRecyclerActivity_title));

        final ExpandableRecyclerView recyclerView = (ExpandableRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final ExpandableFruitAdapter fruitAdapter = new ExpandableFruitAdapter(fruits);
        recyclerView.setAdapter(fruitAdapter);
    }
}
