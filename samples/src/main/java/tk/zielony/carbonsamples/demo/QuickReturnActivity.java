package tk.zielony.carbonsamples.demo;

import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import carbon.Carbon;
import carbon.component.DefaultHeaderItem;
import carbon.component.DefaultImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.component.PaddedHeaderRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.FloatingActionButton;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DrawableImageGenerator;
import tk.zielony.randomdata.common.StringDateGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class QuickReturnActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickreturn);

        List<Serializable> items = Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_toolbarHeight) + getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultHeaderItem("Header"),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem(),
                new DefaultImageTextSubtextDateItem());

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableImageGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultImageTextSubtextDateItem.class)),
                new TextGenerator().withMatcher(f -> f.getName().equals("subtext")),
                new StringDateGenerator()
        });
        randomData.fill(items);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setEdgeEffectOffsetTop(getResources().getDimension(R.dimen.carbon_toolbarHeight));
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultImageTextSubtextDateItem.class, ImageTextSubtextDateRow::new);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DefaultHeaderItem.class, PaddedHeaderRow::new);
        recycler.setAdapter(adapter);
        adapter.setItems(items);

        final FloatingActionButton fab = findViewById(R.id.fab);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.quickReturnActivity_title));
        recycler.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            int yScroll = 0;

            @Override
            public void onScrolled(androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab.getVisibility() == View.VISIBLE && fab.getAnimator() == null && yScroll > 50 * Carbon.getDip(getApplicationContext())) {
                    yScroll = 0;
                    fab.animateVisibility(View.GONE);
                    toolbar.animateVisibility(View.GONE);
                }
                if (fab.getVisibility() != View.VISIBLE && fab.getAnimator() == null && yScroll < -50 * Carbon.getDip(getApplicationContext())) {
                    yScroll = 0;
                    fab.animateVisibility(View.VISIBLE);
                    toolbar.animateVisibility(View.VISIBLE);
                }
                if (Math.signum(dy) != Math.signum(yScroll))
                    yScroll = 0;
                yScroll += dy;
            }
        });
    }
}
