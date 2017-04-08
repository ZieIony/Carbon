package tk.zielony.carbonsamples.component;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import carbon.component.AvatarTextRow;
import carbon.component.ComponentItem;
import carbon.component.DefaultAvatarTextItem;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class AvatarTextListItemActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.avatarTextListItemActivity_title));

        List<ComponentItem> items = Arrays.asList(
                new DefaultAvatarTextItem(),
                new DefaultAvatarTextItem(),
                new DividerItem(),
                new DefaultAvatarTextItem(),
                new DefaultAvatarTextItem());

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        randomData.fill(items);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        RowListAdapter adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow.FACTORY);
        adapter.addFactory(DividerItem.class, DividerRow.FACTORY);
        recycler.setAdapter(adapter);
        adapter.setItems(items);
    }
}
