package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;

import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.internal.FloatingMenuBuilder;

public class FloatingActionMenu extends PopupWindow {
    private final Handler handler;
    private final LinearLayout content;
    private FloatingMenuBuilder menu;

    MenuItem.OnMenuItemClickListener listener;
    View anchor;

    public FloatingActionMenu(Context context) {
        super(new LinearLayout(context), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        content = (LinearLayout) getContentView();
        content.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.setOrientation(android.widget.LinearLayout.VERTICAL);
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

    public void build() {
        content.removeAllViews();
        for (int i = 0; i < menu.size(); i++)
            ((FloatingMenuItem) menu.getItem(i)).build();
    }

    public boolean show() {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        WindowManager wm = (WindowManager) anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        boolean left = location[0] < display.getWidth() + anchor.getWidth() - location[0];
        boolean top = location[1] < display.getHeight() + anchor.getHeight() - location[1];

        content.setGravity(left ? Gravity.LEFT : Gravity.RIGHT);
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

        ArrayList<LinearLayout> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isVisible())
                items.add((LinearLayout) content.getChildAt(i));
        }

        for (int i = 0; i < items.size(); i++) {
            LinearLayout item = items.get(i);
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
        CarbonContextWrapper contextWrapper = new CarbonContextWrapper(getContentView().getContext());
        this.menu = new FloatingMenuBuilder(this);
        MenuInflater inflater = new MenuInflater(contextWrapper);
        inflater.inflate(resId, menu);
    }

    public void setMenu(final Menu baseMenu) {
        this.menu = new FloatingMenuBuilder(this);
        for (int i = 0; i < baseMenu.size(); i++) {
            MenuItem menuItem = baseMenu.getItem(i);
            this.menu.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle()).setIcon(menuItem.getIcon()).setVisible(menuItem.isVisible()).setEnabled(menuItem.isEnabled());
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }
}
