package tk.zielony.carbonsamples.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Arrays;

import carbon.Carbon;
import carbon.component.IconSearchData;
import carbon.component.IconSearchRow;
import carbon.component.IconTextItem;
import carbon.component.IconTextRow;
import carbon.drawable.VectorDrawable;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.RowListAdapter;
import carbon.widget.ArraySearchDataProvider;
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
        RowListAdapter adapter = new RowListAdapter<>(SampleItem.class, IconTextRow::new);
        adapter.addFactory(IconSearchData.class, parent -> new IconSearchRow(parent, new ArraySearchDataProvider(new String[]{}), filterResults -> {

        }));
        recycler.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(new ColorDrawable(Carbon.getThemeColor(this, R.attr.carbon_dividerColor)), getResources().getDimensionPixelSize(R.dimen.carbon_1dip));
        dividerItemDecoration.setDrawRules(position -> position == 0);
        recycler.addItemDecoration(dividerItemDecoration);
        adapter.setItems(Arrays.asList(new IconSearchData(), new SampleItem(), new SampleItem(), new SampleItem(), new SampleItem()));
    }
}
