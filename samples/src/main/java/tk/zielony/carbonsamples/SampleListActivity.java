package tk.zielony.carbonsamples;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import carbon.component.DataBindingComponent;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowArrayAdapter;
import carbon.widget.RecyclerView;

public class SampleListActivity extends SamplesActivity {

    RowArrayAdapter<Serializable> adapter;
    Map<String, String> allPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplelist);

        SharedPreferences preferences = getSharedPreferences("samples", Context.MODE_PRIVATE);
        allPreferences = (Map<String, String>) preferences.getAll();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(getResources().getBoolean(R.bool.tablet) ?
                new GridLayoutManager(this, 2) :
                new LinearLayoutManager(this));
        adapter = new RowArrayAdapter<>(SampleActivityGroup.class, parent -> new DataBindingComponent<>(parent, R.layout.row_main));
        adapter.addFactory(SampleActivityItem.class, parent -> {
            return new DataBindingComponent<SampleActivityItem>(parent, R.layout.row_sample) {
                @Override
                public void bind(SampleActivityItem data) {
                    super.bind(data);
                    getView().findViewById(R.id.star).setOnClickListener(v -> {
                        data.setStarred(!data.isStarred());
                        if (data.isStarred()) {
                            preferences.edit().putString(data.getActivityClass().getName(), data.getName()).commit();
                        } else {
                            preferences.edit().remove(data.getActivityClass().getName()).commit();
                        }
                        for (int i = 0; i < adapter.getItemCount(); i++) {
                            if (adapter.getItems()[i] == data) {
                                adapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    });
                }
            };
        });
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(String.class, parent -> new DataBindingComponent<>(parent, R.layout.row_description));
        adapter.setOnItemClickedListener((view, item, position) -> {
            if (item instanceof SampleActivityGroup) {
                Class activityClass = ((SampleActivityGroup) item).getActivityClass();
                Intent intent = new Intent(view.getContext(), activityClass);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    protected void setItems(Serializable[] items) {
        for (Serializable item : items) {
            if (item instanceof SampleActivityItem) {
                SampleActivityItem sampleItem = (SampleActivityItem) item;
                sampleItem.setStarred(allPreferences.containsKey(sampleItem.getActivityClass().getName()));
            }
        }

        adapter.setItems(items);

    }

}
