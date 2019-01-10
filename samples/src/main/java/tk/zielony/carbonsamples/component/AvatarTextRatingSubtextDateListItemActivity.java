package tk.zielony.carbonsamples.component;

import android.os.Bundle;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import carbon.component.AvatarTextRatingSubtextDateRow;
import carbon.component.DefaultAvatarTextRatingSubtextDateItem;
import carbon.component.DefaultHeaderItem;
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
import tk.zielony.randomdata.common.IntegerGenerator;
import tk.zielony.randomdata.common.StringDateGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class AvatarTextRatingSubtextDateListItemActivity extends SamplesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.avatarTextRatingSubtextDateListItemActivity_title));

        List<Serializable> items = Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultHeaderItem("Header"),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new DefaultAvatarTextRatingSubtextDateItem(),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultAvatarTextRatingSubtextDateItem.class)),
                new IntegerGenerator(0, 5).withMatcher(f -> f.getName().equals("rating")),
                new TextGenerator().withMatcher(f -> f.getName().equals("subtext")),
                new StringDateGenerator()
        });
        randomData.fill(items);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultAvatarTextRatingSubtextDateItem.class, AvatarTextRatingSubtextDateRow::new);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DefaultHeaderItem.class, PaddedHeaderRow::new);


        recycler.setAdapter(adapter);
        adapter.setItems(items);
    }
}
