package tk.zielony.carbonsamples.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import carbon.component.DefaultHeaderItem;
import carbon.component.DefaultImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.component.PaddedHeaderRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DrawableImageGenerator;
import tk.zielony.randomdata.common.StringDateGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class ImageTextSubtextDateListItemActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.imageTextSubtextDateListItemActivity_title));

        List<Serializable> items = Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableImageGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultImageTextSubtextDateItem.class)),
                new TextGenerator().withMatcher(f -> f.getName().equals("subtext")),
                new StringDateGenerator()
        });
        randomData.fill(items);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        RowListAdapter<Serializable> adapter = new RowListAdapter<Serializable>(DefaultImageTextSubtextDateItem.class, ImageTextSubtextDateRow::new);
        adapter.addFactory(PaddingItem.class, PaddingRow.FACTORY);
        adapter.addFactory(DefaultHeaderItem.class, PaddedHeaderRow.FACTORY);


        recycler.setAdapter(adapter);
        adapter.setItems(items);
    }
}
