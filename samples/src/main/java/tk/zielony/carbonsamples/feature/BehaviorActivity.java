package tk.zielony.carbonsamples.feature;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import carbon.beta.Behavior;
import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.internal.MathUtils;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import carbon.widget.RelativeLayout;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.landscapeview.LandscapeView;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class BehaviorActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);

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
        LandscapeView landscapeView = findViewById(R.id.rect);

        layout.addBehavior(new Behavior<RecyclerView>(recycler) {
            @Override
            public PointF onNestedScroll(float scrollX, float scrollY) {
                recycler.scrollBy((int) scrollX, (int) scrollY);
                return new PointF();
            }
        });

        layout.addBehavior(new Behavior<LandscapeView>(landscapeView) {
            @Override
            public PointF onNestedScroll(float scrollX, float scrollY) {
                int height = getTarget().getHeight();
                int newHeight = (int) MathUtils.constrain(height - scrollY, getResources().getDimension(carbon.R.dimen.carbon_toolbarHeight), getResources().getDimension(carbon.R.dimen.carbon_toolbarHeight) * 4);
                setHeight(newHeight);
                return new PointF(scrollX, scrollY + (newHeight - height));
            }

            private void setHeight(int height) {
                ViewGroup.LayoutParams layoutParams = getTarget().getLayoutParams();
                if (layoutParams == null) {
                    getTarget().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
                } else {
                    layoutParams.height = height;
                    getTarget().setLayoutParams(layoutParams);
                }
            }
        });
    }
}
