package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2015-01-27.
 */
public class Divider extends View {
    public Divider(Context context) {
        this(context, null);
    }

    public Divider(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_dividerStyle);
    }

    public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationBar, defStyleAttr, 0);
        int color = a.getColor(R.styleable.Divider_android_background, 0);
        setBackgroundColor(color);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) Carbon.getDip(getContext()));
    }
}
