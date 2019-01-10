package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import carbon.component.TextRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class PaginationActivity extends SamplesActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));
    RowListAdapter<String> fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagination);

        Samples.initToolbar(this, getString(R.string.paginationActivity_title));

        SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipeRefresh);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fruitAdapter = new RowListAdapter<>(String.class, TextRow::new);
        recyclerView.setAdapter(fruitAdapter);
        recyclerView.setPagination(new RecyclerView.Pagination(layoutManager) {
            @Override
            protected boolean isLoading() {
                return swipeRefresh.isRefreshing();
            }

            @Override
            protected boolean isLastPage() {
                return page == 10;
            }

            @Override
            protected void loadNextPage() {
                swipeRefresh.setRefreshing(true);
                PaginationActivity.this.loadNextPage();
                swipeRefresh.setRefreshing(false);
            }
        });

        swipeRefresh.setOnRefreshListener(() -> {
            fruitAdapter.setItems(new ArrayList<>());
            page = 1;
            loadNextPage();
            swipeRefresh.setRefreshing(false);
        });
    }

    int page = 1;

    private void loadNextPage() {
        List<String> items = new ArrayList<>(fruitAdapter.getItems());
        items.addAll(Stream.of(fruits).map(fruit -> fruit + " " + page).toList());
        fruitAdapter.setItems(items);
        page++;
    }
}
