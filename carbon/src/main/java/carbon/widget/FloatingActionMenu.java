package carbon.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.CarbonContextWrapper;
import carbon.R;

/**
 * Created by Marcin on 2016-02-16.
 */
public class FloatingActionMenu extends PopupWindow {
    private final Handler handler;
    private final LinearLayout content;
    private Menu menu;

    MenuItem.OnMenuItemClickListener listener;
    private View anchor;

    public FloatingActionMenu(Context context) {
        super(new LinearLayout(context), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        content = (LinearLayout) getContentView();
        content.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.setOrientation(android.widget.LinearLayout.VERTICAL);
        content.setPadding(0, content.getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf), 0, content.getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf));

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

    public void build() {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        WindowManager wm = (WindowManager) anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        boolean left = location[0] < display.getWidth() + anchor.getWidth() - location[0];

        content.removeAllViews();

        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            LayoutInflater inflater = LayoutInflater.from(content.getContext());
            final LinearLayout view = (LinearLayout) inflater.inflate(left ? R.layout.carbon_floatingactionmenu_left : R.layout.carbon_floatingactionmenu_right, content, false);
            TextView tooltip = (TextView) view.findViewById(R.id.carbon_tooltip);
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.carbon_fab);

            tooltip.setText(item.getTitle());
            fab.setImageDrawable(item.getIcon());
            fab.setOnClickListener(v -> {
                if (listener != null)
                    listener.onMenuItemClick(item);
                dismiss();
            });
            content.addView(view);

            view.setVisibilityImmediate(View.INVISIBLE);
        }

        content.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    public boolean show() {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        WindowManager wm = (WindowManager) anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        boolean left = location[0] < display.getWidth() + anchor.getWidth() - location[0];
        boolean top = location[1] < display.getHeight() + anchor.getHeight() - location[1];

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

        for (int i = 0; i < menu.size(); i++) {
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    content.getChildAt(finalI).setVisibility(View.VISIBLE);
                }
            }, top ? i * 50 : (menu.size() - 1 - i) * 50);
        }

        return true;
    }

    @Override
    public void dismiss() {
        final LinearLayout content = (LinearLayout) getContentView();
        LinearLayout child = null;
        for (int i = 0; i < content.getChildCount(); i++) {
            child = (LinearLayout) content.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
        }
        if (child != null) {
            child.getAnimator().addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dismissImmediate();
                }
            });
        }
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(new CarbonContextWrapper(getContentView().getContext()));
        MenuInflater inflater = new MenuInflater(getContentView().getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }
}
