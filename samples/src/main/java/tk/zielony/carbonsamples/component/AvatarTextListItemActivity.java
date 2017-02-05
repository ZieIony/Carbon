package tk.zielony.carbonsamples.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Arrays;

import carbon.component.AvatarTextItem;
import carbon.component.AvatarTextRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-02.
 */

public class AvatarTextListItemActivity extends AppCompatActivity {
    class SampleItem implements AvatarTextItem {

        @Override
        public Drawable getAvatar(Context context) {
            return context.getResources().getDrawable(R.drawable.ic_launcher);
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
        RowListAdapter<SampleItem> adapter = new RowListAdapter<>(SampleItem.class, AvatarTextRow::new);
        recycler.setAdapter(adapter);
        adapter.setItems(Arrays.asList(new SampleItem(), new SampleItem(), new SampleItem(), new SampleItem()));
    }
}
