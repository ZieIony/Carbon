package tk.zielony.carbonsamples.component;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Arrays;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

/**
 * Created by Marcin on 2017-02-02.
 */

public class AvatarTextListItemActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.avatarTextListItemActivity_title));

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        RowListAdapter adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow.FACTORY);
        adapter.addFactory(DividerItem.class, DividerRow.FACTORY);
        recycler.setAdapter(adapter);
        Drawable avatar = getResources().getDrawable(R.drawable.iceland);
        adapter.setItems(Arrays.asList(
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text"),
                new DividerItem(),
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text")));
    }
}
