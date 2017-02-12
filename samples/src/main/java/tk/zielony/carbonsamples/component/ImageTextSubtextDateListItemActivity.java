package tk.zielony.carbonsamples.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import carbon.component.DefaultHeaderItem;
import carbon.component.ImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.component.PaddedHeaderRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-02.
 */

public class ImageTextSubtextDateListItemActivity extends AppCompatActivity {
    static SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd MMM");

    class SampleItem implements ImageTextSubtextDateItem {

        @Override
        public Drawable getImage(Context context) {
            return context.getResources().getDrawable(R.drawable.watermelon);
        }

        @Override
        public String getText() {
            return "Sample text";
        }

        @Override
        public String getSubtext() {
            return "Sample description";
        }

        @Override
        public String getDate() {
            return format.format(new Date().getTime());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter adapter = new RowListAdapter<>(SampleItem.class, ImageTextSubtextDateRow::new);
        adapter.addFactory(DefaultHeaderItem.class, PaddedHeaderRow.FACTORY);
        recycler.setAdapter(adapter);
        adapter.setItems(Arrays.asList(new DefaultHeaderItem("Header"), new SampleItem(), new SampleItem(), new DefaultHeaderItem("Header"), new SampleItem(), new SampleItem()));
    }
}
