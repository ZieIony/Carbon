package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.Arrays;

import carbon.component.AvatarTextRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.component.AvatarTextListItemActivity;

/**
 * Created by Marcin on 2017-02-09.
 */

public class ScrollRecycler extends RecyclerView implements ScrollChild {
    public ScrollRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        RowListAdapter<AvatarTextListItemActivity.SampleItem> adapter = new RowListAdapter<>(AvatarTextListItemActivity.SampleItem.class, AvatarTextRow::new);
        setAdapter(adapter);
        adapter.setItems(Arrays.asList(new AvatarTextListItemActivity.SampleItem(), new AvatarTextListItemActivity.SampleItem(), new AvatarTextListItemActivity.SampleItem(), new AvatarTextListItemActivity.SampleItem()));
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
