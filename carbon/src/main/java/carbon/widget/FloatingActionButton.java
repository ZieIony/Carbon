package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Menu;

import carbon.R;
import carbon.animation.AnimUtils;

/**
 * FAB implementation using an ImageView and rounded corners. Supports SVGs, animated shadows,
 * ripples and other material features.
 */
public class FloatingActionButton extends ImageView {
    FloatingActionMenu floatingActionMenu;

    public FloatingActionButton(Context context) {
        super(context, null, R.attr.carbon_fabStyle);
        initFloatingActionButton(null, R.attr.carbon_fabStyle, R.style.carbon_FloatingActionButton);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_fabStyle);
        initFloatingActionButton(attrs, R.attr.carbon_fabStyle, R.style.carbon_FloatingActionButton);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFloatingActionButton(attrs, defStyleAttr, R.style.carbon_FloatingActionButton);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFloatingActionButton(attrs, defStyleAttr, defStyleRes);
    }

    private void initFloatingActionButton(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        AnimUtils.setupElevationAnimator(getStateAnimator(), this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, defStyleRes);

        if (a.hasValue(R.styleable.FloatingActionButton_carbon_menu)) {
            int resId = a.getResourceId(R.styleable.FloatingActionButton_carbon_menu, 0);
            if (resId != 0)
                setMenu(resId);
        }

        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        invalidateMenu();
    }

    public void invalidateMenu() {
        if (floatingActionMenu != null)
            floatingActionMenu.invalidate();
    }

    public void setMenu(int resId) {
        floatingActionMenu = new FloatingActionMenu(getContext());
        floatingActionMenu.setMenu(resId);
        floatingActionMenu.setAnchor(this);

        setOnClickListener(__ -> floatingActionMenu.show());
    }

    public void setMenu(Menu menu) {
        if (menu != null) {
            floatingActionMenu = new FloatingActionMenu(getContext());
            floatingActionMenu.setMenu(menu);
            floatingActionMenu.setAnchor(this);

            setOnClickListener(__ -> floatingActionMenu.show());
        } else {
            floatingActionMenu = null;
            setOnClickListener(null);
        }
    }

    public void setMenuItems(FloatingActionMenu.Item[] items) {
        floatingActionMenu = new FloatingActionMenu(getContext());
        floatingActionMenu.setMenuItems(items);
        floatingActionMenu.setAnchor(this);

        setOnClickListener(__ -> floatingActionMenu.show());
    }

    public FloatingActionMenu getFloatingActionMenu() {
        return floatingActionMenu;
    }

    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener<FloatingActionMenu.Item> listener) {
        if (floatingActionMenu != null)
            floatingActionMenu.setOnItemClickedListener(listener);
    }

}
