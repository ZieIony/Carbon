package tk.zielony.carbonsamples;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import carbon.component.DataBindingComponent;
import carbon.recycler.RowListAdapter;
import carbon.recycler.ViewItemDecoration;
import carbon.widget.RecyclerView;

public class SampleListActivity extends ThemedActivity {

    RowListAdapter<Serializable> adapter;
    Map<String, String> allPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("samples", Context.MODE_PRIVATE);
        allPreferences = (Map<String, String>) preferences.getAll();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RowListAdapter<>();
        adapter.putFactory(SampleActivityGroup.class, parent -> new DataBindingComponent<>(parent, R.layout.row_main));
        adapter.putFactory(SampleActivityItem.class, parent -> {
            return new DataBindingComponent<SampleActivityItem>(parent, R.layout.row_sample) {
                @Override
                public void bind(SampleActivityItem data) {
                    super.bind(data);
                    getView().findViewById(R.id.star).setOnClickListener(v -> {
                        data.setStarred(!data.isStarred());
                        if (data.isStarred()) {
                            preferences.edit().putString(data.getActivityClass().getName(), getString(data.getName())).commit();
                        } else {
                            preferences.edit().remove(data.getActivityClass().getName()).commit();
                        }
                        for (int i = 0; i < adapter.getItemCount(); i++) {
                            if (adapter.getItems().get(i) == data) {
                                adapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    });
                }
            };
        });
        adapter.putFactory(String.class, parent -> new DataBindingComponent<>(parent, R.layout.row_description));
        adapter.setOnItemClickedListener((view, item, position) -> {
            if (item instanceof SampleActivityGroup) {
                Class activityClass = ((SampleActivityGroup) item).getActivityClass();
                Intent intent = new Intent(view.getContext(), activityClass);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        ViewItemDecoration itemDecoration = new ViewItemDecoration(this, R.layout.carbon_row_padding);
        itemDecoration.setDrawBefore(position -> position == 0);
        itemDecoration.setDrawAfter(position -> position == adapter.getItemCount() - 1);
        recyclerView.addItemDecoration(itemDecoration);
    }

    protected void setItems(List<Serializable> items) {
        for (Serializable item : items) {
            if (item instanceof SampleActivityItem) {
                SampleActivityItem sampleItem = (SampleActivityItem) item;
                sampleItem.setStarred(allPreferences.containsKey(sampleItem.getActivityClass().getName()));
            }
        }

        adapter.setItems(items);
    }

}
