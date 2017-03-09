package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.Arrays;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-09.
 */

public class ScrollRecycler extends RecyclerView implements ScrollChild {
    public ScrollRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        RowListAdapter<DefaultAvatarTextItem> adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow::new);
        setAdapter(adapter);
        Drawable avatar = getResources().getDrawable(R.drawable.iceland);
        adapter.setItems(Arrays.asList(
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text"),
                new DefaultAvatarTextItem(avatar, "text")));
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
