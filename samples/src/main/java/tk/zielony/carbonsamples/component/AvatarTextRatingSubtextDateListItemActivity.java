package tk.zielony.carbonsamples.component;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import carbon.component.AvatarTextRatingSubtextDateRow;
import carbon.component.DefaultAvatarTextRatingSubtextDateItem;
import carbon.component.DefaultHeaderItem;
import carbon.component.PaddedHeaderRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DateGenerator;
import tk.zielony.randomdata.common.IntegerGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;
import tk.zielony.randomdata.transformer.DateToStringTransformer;

@SampleAnnotation(layoutId = R.layout.activity_listcomponent, titleId = R.string.avatarTextRatingSubtextDateListItemActivity_title)
public class AvatarTextRatingSubtextDateListItemActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        RandomData randomData = new RandomData();
        randomData.addGenerator(Drawable.class, new DrawableAvatarGenerator(this));
        randomData.addGenerator(String.class, new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultAvatarTextRatingSubtextDateItem.class)));
        randomData.addGenerator(Integer.class, new IntegerGenerator(0, 5).withMatcher(f -> f.getName().equals("rating")));
        randomData.addGenerator(String.class, new TextGenerator().withMatcher(f -> f.getName().equals("subtext")));
        randomData.addGenerator(String.class, new DateGenerator().withTransformer(new DateToStringTransformer()).withMatcher(f -> f.getName().equals("date")));

        List<Serializable> items = Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultHeaderItem("Header"),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                new DefaultHeaderItem("Header"),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                new DefaultHeaderItem("Header"),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                new DefaultHeaderItem("Header"),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                randomData.generate(DefaultAvatarTextRatingSubtextDateItem.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultAvatarTextRatingSubtextDateItem.class, AvatarTextRatingSubtextDateRow::new);
        adapter.putFactory(PaddingItem.class, PaddingRow::new);
        adapter.putFactory(DefaultHeaderItem.class, PaddedHeaderRow::new);


        recycler.setAdapter(adapter);
        adapter.setItems(items);
    }
}
