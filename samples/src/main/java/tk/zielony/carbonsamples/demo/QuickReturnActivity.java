package tk.zielony.carbonsamples.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.component.DefaultHeaderItem;
import carbon.component.DefaultImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.component.PaddedHeaderRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.FloatingActionButton;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DateGenerator;
import tk.zielony.randomdata.common.DrawableImageGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;
import tk.zielony.randomdata.transformer.DateToStringTransformer;

@SampleAnnotation(layoutId = R.layout.activity_quickreturn, titleId = R.string.quickReturnActivity_title)
public class QuickReturnActivity extends ThemedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Serializable> items = Arrays.asList(
                //new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_toolbarHeight) + getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
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
        randomData.addGenerator(Drawable.class, new DrawableImageGenerator(this));
        randomData.addGenerator(String.class, new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultImageTextSubtextDateItem.class)));
        randomData.addGenerator(String.class, new TextGenerator().withMatcher(f -> f.getName().equals("subtext")));
        randomData.addGenerator(String.class, new DateGenerator().withTransformer(new DateToStringTransformer()));
        randomData.fill(items);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultImageTextSubtextDateItem.class, ImageTextSubtextDateRow::new);
        adapter.putFactory(DefaultHeaderItem.class, PaddedHeaderRow::new);
        recycler.setAdapter(adapter);
        adapter.setItems(items);

        final FloatingActionButton fab = findViewById(R.id.fab);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
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
