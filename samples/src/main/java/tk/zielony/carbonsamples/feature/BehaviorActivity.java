package tk.zielony.carbonsamples.feature;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import carbon.behavior.HeightBehavior;
import carbon.behavior.RecyclerScrollBehavior;
import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import carbon.widget.RelativeLayout;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.landscapeview.LandscapeView;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

@ActivityAnnotation(layout = R.layout.activity_behavior, title = R.string.behaviorActivity_title)
public class BehaviorActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter<DefaultAvatarTextItem> adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow::new);
        recycler.setAdapter(adapter);
        List<DefaultAvatarTextItem> items = Stream.generate(DefaultAvatarTextItem::new).limit(10).collect(Collectors.toList());

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        randomData.fill(items);

        adapter.setItems(items);

        RelativeLayout layout = findViewById(R.id.layout);
        LandscapeView landscapeView = findViewById(R.id.landscape);

        float minHeight = getResources().getDimension(carbon.R.dimen.carbon_toolbarHeight);
        float maxHeight = minHeight * 4;

        layout.addBehavior(new HeightBehavior<>(landscapeView, minHeight, maxHeight, HeightBehavior.Direction.Up));
        layout.addBehavior(new RecyclerScrollBehavior(recycler));
        layout.addBehavior(new HeightBehavior<>(landscapeView, minHeight, maxHeight, HeightBehavior.Direction.Down));
    }
}
