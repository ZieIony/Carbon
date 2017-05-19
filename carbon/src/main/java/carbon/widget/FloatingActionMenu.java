package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import carbon.Carbon;
import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.component.FloatingActionMenuLeftRow;
import carbon.component.FloatingActionMenuRightRow;
import carbon.component.MenuItem;
import carbon.internal.Menu;
import carbon.recycler.RowListAdapter;

public class FloatingActionMenu extends PopupWindow {
    private final Handler handler;
    private final RecyclerView content;
    private Menu menu;

    android.view.MenuItem.OnMenuItemClickListener listener;
    private View anchor;

    private RowListAdapter adapter;

    public FloatingActionMenu(Context context) {
        super(new RecyclerView(new CarbonContextWrapper(context)), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        adapter = new RowListAdapter<>(MenuItem.class, left ? FloatingActionMenuLeftRow.FACTORY : FloatingActionMenuRightRow.FACTORY);
        content.setAdapter(adapter);
        adapter.setItems(menu.getVisibleItems());
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickedListener((view, o, position) -> {
            if (listener != null)
                listener.onMenuItemClick(menu.getItem(position));
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
            int delay = top ? i * 50 : (menu.size() - 1 - i) * 50;
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
        menu = Carbon.getMenu(getContentView().getContext(), resId);
    }

    public void setMenu(final android.view.Menu baseMenu) {
        menu = Carbon.getMenu(getContentView().getContext(), baseMenu);
    }

    public android.view.Menu getMenu() {
        return menu;
    }

    public void setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }
}
