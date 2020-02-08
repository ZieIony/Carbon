package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import carbon.recycler.DragTouchHelper;
import carbon.recycler.SwipeTouchHelper;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(
        layoutId = R.layout.activity_recycler,
        titleId = R.string.recyclerViewActivity_title,
        iconId = R.drawable.ic_view_stream_black_24dp
)
public class RecyclerActivity extends ThemedActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));
    FruitAdapter fruitAdapter;
    DragTouchHelper<String> dragTouchHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fruitAdapter = new FruitAdapter(fruits, (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                dragTouchHelper.startDrag(v);
            return true;
        });
        dragTouchHelper = new DragTouchHelper<>(recyclerView, fruitAdapter);
        recyclerView.setAdapter(fruitAdapter);

        dragTouchHelper.setOnItemMovedListener((item, position, targetPosition) -> {
            Collections.swap(fruits, position, targetPosition);
            fruitAdapter.notifyItemMoved(position, targetPosition);
            return true;
        });

        SwipeTouchHelper<String> swipeTouchHelper = new SwipeTouchHelper<>(recyclerView, fruitAdapter);
        swipeTouchHelper.setOnItemSwipedListener((item, position) -> {
            fruits.remove(position);
            fruitAdapter.notifyItemRemoved(position);
        });
    }
}
