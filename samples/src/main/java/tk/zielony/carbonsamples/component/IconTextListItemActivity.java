package tk.zielony.carbonsamples.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Arrays;

import carbon.drawable.VectorDrawable;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-02.
 */

public class IconTextListItemActivity extends AppCompatActivity {
    class SampleItem implements IconTextItem {

        @Override
        public Drawable getIcon(Context context) {
            return new VectorDrawable(context.getResources(), R.raw.ic_face_24px);
        }

        @Override
        public String getText() {
            return "Sample text";
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter<SampleItem> adapter = new RowListAdapter<>(SampleItem.class, IconTextRow::new);
        recycler.setAdapter(adapter);
        adapter.setItems(Arrays.asList(new SampleItem(), new SampleItem(), new SampleItem(), new SampleItem()));
    }
}
