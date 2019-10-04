package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.component.FloatingActionMenuLeftRow;
import carbon.component.FloatingActionMenuRightRow;
import carbon.recycler.RowArrayAdapter;

public class FloatingActionMenu extends PopupWindow {
    public static class Item implements Serializable {
        Drawable icon;
        ColorStateList tint;
        Drawable background;
        private boolean enabled;
        private CharSequence title;

        public Item(MenuItem item) {
            icon = item.getIcon();
            tint = MenuItemCompat.getIconTintList(item);
            enabled = item.isEnabled();
            title = item.getTitle();
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIconTintList(ColorStateList tint) {
            this.tint = tint;
        }

        public ColorStateList getIconTintList() {
            return tint;
        }

        public void setBackgroundDrawable(Drawable background) {
            this.background = background;
        }

        public Drawable getBackgroundDrawable() {
            return background;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    private final Handler handler;
    private final RecyclerView content;
    private Item[] items;

    RecyclerView.OnItemClickedListener<Item> listener;
    private View anchor;

    private RowArrayAdapter<Item> adapter;

    public FloatingActionMenu(Context context) {
        super(new RecyclerView(CarbonContextWrapper.wrap(context)), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        content = (RecyclerView) getContentView();
        content.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.setLayoutManager(new LinearLayoutManager(context));
        content.setPadding(0, content.getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf), 0, content.getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf));
        content.setOutAnimator(AnimUtils.getFadeOutAnimator());

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
        setClippingEnabled(false);

        handler = new Handler();
    }

    public void setAnchor(View anchor) {
        this.anchor = anchor;
    }

    public View getAnchor() {
        return anchor;
    }

    public void invalidate() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public boolean show() {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        WindowManager wm = (WindowManager) anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        boolean left = location[0] < display.getWidth() + anchor.getWidth() - location[0];
        boolean top = location[1] < display.getHeight() + anchor.getHeight() - location[1];

        adapter = new RowArrayAdapter<>(items, left ? FloatingActionMenuLeftRow::new : FloatingActionMenuRightRow::new);
        content.setAdapter(adapter);

        adapter.setOnItemClickedListener((view, o, position) -> {
            if (listener != null)
                listener.onItemClicked(view, o, position);
            dismiss();
        });

        content.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        super.showAtLocation(anchor, Gravity.TOP | Gravity.LEFT, 0, 0);
        if (!left & top) {  // right top
            update(location[0] - content.getMeasuredWidth() + anchor.getWidth(), location[1] + anchor.getHeight(), content.getMeasuredWidth(), content.getMeasuredHeight());
        } else if (!left & !top) {  // right bottom
            update(location[0] - content.getMeasuredWidth() + anchor.getWidth(), location[1] - content.getMeasuredHeight(), content.getMeasuredWidth(), content.getMeasuredHeight());
        } else if (left & !top) { // left bottom
            update(location[0], location[1] - content.getMeasuredHeight(), content.getMeasuredWidth(), content.getMeasuredHeight());
        } else {    // left top
            update(location[0], location[1] + anchor.getHeight(), content.getMeasuredWidth(), content.getMeasuredHeight());
        }

        for (int i = 0; i < content.getChildCount(); i++) {
            LinearLayout item = (LinearLayout) content.getChildAt(i);
            item.setVisibility(View.INVISIBLE);
            int delay = top ? i * 50 : (items.length - 1 - i) * 50;
            handler.postDelayed(() -> item.animateVisibility(View.VISIBLE), delay);
        }

        content.setAlpha(1);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void dismiss() {
        content.animateVisibility(View.INVISIBLE).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediate();
            }
        });
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    public void setMenu(int resId) {
        setMenu(Carbon.getMenu(getContentView().getContext(), resId));
    }

    public void setMenu(final android.view.Menu menu) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isVisible())
                items.add(new Item(menu.getItem(i)));
        }

        this.items = Stream.of(items).toArray(Item[]::new);
    }

    public void setMenuItems(Item[] items) {
        this.items = items;
    }

    public Item[] getMenuItems() {
        return items;
    }

    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener<Item> listener) {
        this.listener = listener;
    }
}
