package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import carbon.R;

/**
 * Created by Marcin on 2015-01-04.
 */
public class StatusBar extends View {
    public StatusBar(Context context) {
        super(context);
        init(null, R.attr.carbon_statusBarStyle);
    }

    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_statusBarStyle);
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
        if(isInEditMode())
            return;

        setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) getContext()).getWindow();
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StatusBar, defStyle, 0);
            int color = a.getColor(R.styleable.StatusBar_android_background, 0);
            a.recycle();

            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            try {
                Method setStatusBarColorMethod = window.getClass().getMethod("setStatusBarColor", int.class);
                setStatusBarColorMethod.invoke(window, color);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
