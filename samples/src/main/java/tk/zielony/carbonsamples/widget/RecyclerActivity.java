package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import carbon.widget.ItemTouchHelper;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.applibrary.FruitAdapter;

/**
 * Created by Marcin on 2015-05-16.
 */
public class RecyclerActivity extends Activity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));
    FruitAdapter fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(android.support.v7.widget.RecyclerView recyclerView, android.support.v7.widget.RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(android.support.v7.widget.RecyclerView recyclerView, android.support.v7.widget.RecyclerView.ViewHolder viewHolder, android.support.v7.widget.RecyclerView.ViewHolder target) {
                Collections.swap(fruits, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                fruitAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                fruits.remove(viewHolder.getAdapterPosition());
                fruitAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void onSelectedChanged(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fruitAdapter = new FruitAdapter(fruits, itemTouchHelper);
        recyclerView.setAdapter(fruitAdapter);

        recyclerView.setHeader(R.layout.header_scrollview);
    }
}
