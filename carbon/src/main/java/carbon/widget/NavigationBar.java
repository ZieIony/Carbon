package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import carbon.R;

/**
 * Created by Marcin on 2015-01-04.
 */
@Deprecated
public class NavigationBar extends View {
    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_navigationBarStyle);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getNavigationBarHeight(getContext(), getResources().getConfiguration().orientation));
    }

    private int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (isInEditMode())
            return;

        setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;

        setVisibility(VISIBLE);
        Window window = ((Activity) getContext()).getWindow();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationBar, defStyle, 0);
        int color = a.getColor(R.styleable.NavigationBar_android_background, 0);
        setBackgroundColor(color);
        a.recycle();

        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}
