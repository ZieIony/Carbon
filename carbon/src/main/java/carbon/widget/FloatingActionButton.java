package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import carbon.R;
import carbon.animation.AnimUtils;
import carbon.drawable.DefaultAccentColorStateList;

/**
 * Created by Marcin on 2014-12-04.
 * <p/>
 * FAB implementation using an ImageView and rounded corners. Supports SVGs, animated shadows, ripples
 * and other material features.
 */
public class FloatingActionButton extends ImageView {
    FloatingActionMenu floatingActionMenu;
    private Menu menu;

    public FloatingActionButton(Context context) {
        super(context, null, R.attr.carbon_fabStyle);
        initFloatingActionButton(null, R.attr.carbon_fabStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_fabStyle);
        initFloatingActionButton(attrs, R.attr.carbon_fabStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFloatingActionButton(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFloatingActionButton(attrs, defStyleAttr);
    }

    private void initFloatingActionButton(AttributeSet attrs, int defStyleAttr) {
        AnimUtils.setupElevationAnimator(getStateAnimator(), this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, R.style.carbon_FloatingActionButton);
        setCornerRadius((int) a.getDimension(R.styleable.FloatingActionButton_carbon_cornerRadius, -1));

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

        if (getCornerRadius() < 0)
            setCornerRadius(Math.min(getWidth(), getHeight()) / 2);

        invalidateMenu();
    }

    public void invalidateMenu() {
        if (floatingActionMenu != null)
            floatingActionMenu.build();
    }

    public void setMenu(int resId) {
        floatingActionMenu = new FloatingActionMenu(getContext());
        floatingActionMenu.setMenu(resId);
        floatingActionMenu.setAnchor(this);

        this.menu = floatingActionMenu.getMenu();

        setOnClickListener(__ -> floatingActionMenu.show());
    }

    public void setMenu(Menu menu) {
        this.menu = menu;

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

    public Menu getMenu() {
        return menu;
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        if (floatingActionMenu != null)
            floatingActionMenu.setOnMenuItemClickListener(listener);
    }

    @Override
    public void setBackgroundTint(int color) {
        if (color == 0) {
            setBackgroundTint(new DefaultAccentColorStateList(getContext()));
        } else {
            setBackgroundTint(ColorStateList.valueOf(color));
        }
    }

}
