package carbon.widget;

import android.app.Activity;
import android.content.Context;
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
public class StatusBar extends View {
    public StatusBar(Context context) {
        this(context, null);
    }

    public StatusBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_statusBarStyle);
    }

    public StatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) getResources().getDimension(R.dimen.carbon_statusBarHeight));
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (isInEditMode())
            return;

        setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;

        setVisibility(VISIBLE);
        Window window = ((Activity) getContext()).getWindow();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StatusBar, defStyle, 0);
        int color = a.getColor(R.styleable.StatusBar_android_background, 0);
        setBackgroundColor(color);
        a.recycle();

        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
