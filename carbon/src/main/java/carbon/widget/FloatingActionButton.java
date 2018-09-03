package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import carbon.Carbon;
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
        initFloatingActionButton(null, R.attr.carbon_fabStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FloatingActionButton, R.attr.carbon_fabStyle, R.styleable.FloatingActionButton_carbon_theme), attrs, R.attr.carbon_fabStyle);
        initFloatingActionButton(attrs, R.attr.carbon_fabStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FloatingActionButton, defStyleAttr, R.styleable.FloatingActionButton_carbon_theme), attrs, defStyleAttr);
        initFloatingActionButton(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.FloatingActionButton, defStyleAttr, R.styleable.FloatingActionButton_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initFloatingActionButton(attrs, defStyleAttr);
    }

    private void initFloatingActionButton(AttributeSet attrs, int defStyleAttr) {
        AnimUtils.setupElevationAnimator(getStateAnimator(), this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, R.style.carbon_FloatingActionButton);
        setCornerRadius(a.getDimension(R.styleable.FloatingActionButton_carbon_cornerRadius, -1));

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

    public FloatingActionMenu getFloatingActionMenu() {
        return floatingActionMenu;
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        if (floatingActionMenu != null)
            floatingActionMenu.setOnMenuItemClickListener(listener);
    }

}
