package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.Arrays;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

public class ScrollRecycler extends RecyclerView implements ScrollChild {
    public ScrollRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);

        DefaultAvatarTextItem[] items = new DefaultAvatarTextItem[5];

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(context),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        randomData.fill(items);

        setLayoutManager(new LinearLayoutManager(context));
        RowListAdapter<DefaultAvatarTextItem> adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow::new);
        setAdapter(adapter);
        adapter.setItems(Arrays.asList(items));
    }

    @Override
    public int onNestedScrollByY(int y) {
        scrollBy(0, y);
        return y;
    }

    @Override
    public int getNestedScrollRange() {
        return computeVerticalScrollRange();
    }

    @Override
    public int getNestedScrollY() {
        return getScrollY();
    }
}
