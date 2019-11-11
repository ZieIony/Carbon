package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.recycler.DividerItemDecoration;
import carbon.widget.ExpandableRecyclerView;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_expandablerecycler, titleId = R.string.expandableRecyclerActivity_title)
public class ExpandableRecyclerActivity extends ThemedActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        final ExpandableRecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ExpandableFruitAdapter fruitAdapter = new ExpandableFruitAdapter(fruits);
        recyclerView.setAdapter(fruitAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this);
        decoration.setDrawBefore(position -> position > 0 && fruits.contains(fruitAdapter.getItem(position)));
        recyclerView.addItemDecoration(decoration);
    }
}
