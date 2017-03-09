package tk.zielony.carbonsamples.component;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import carbon.component.DefaultHeaderItem;
import carbon.component.DefaultImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.component.PaddedHeaderRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

/**
 * Created by Marcin on 2017-02-02.
 */

public class ImageTextSubtextDateListItemActivity extends SamplesActivity {
    static SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd MMM");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.imageTextSubtextDateListItemActivity_title));

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter adapter = new RowListAdapter<>(DefaultImageTextSubtextDateItem.class, ImageTextSubtextDateRow::new);
        adapter.addFactory(DefaultHeaderItem.class, PaddedHeaderRow.FACTORY);
        recycler.setAdapter(adapter);
        Drawable drawable = getResources().getDrawable(R.drawable.watermelon);
        String date = format.format(new Date().getTime());
        adapter.setItems(Arrays.asList(
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(drawable, "text", "subtext", date),
                new DefaultImageTextSubtextDateItem(drawable, "text", "subtext", date),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(drawable, "text", "subtext", date),
                new DefaultImageTextSubtextDateItem(drawable, "text", "subtext", date)));
    }
}
