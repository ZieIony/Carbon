package tk.zielony.carbonsamples.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
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

        List<Serializable> items = Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultAvatarTextItem(),
                new DefaultAvatarTextItem(),
                new DividerItem(),
                new DefaultAvatarTextItem(),
                new DefaultAvatarTextItem(),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        randomData.fill(items);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        RowListAdapter adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow.FACTORY);
        adapter.addFactory(PaddingItem.class, PaddingRow.FACTORY);
        adapter.addFactory(DividerItem.class, DividerRow.FACTORY);
        recycler.setAdapter(adapter);
        adapter.setItems(items);
    }
}
