package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2015-01-27.
 * <p/>
 * A very simple class of a divider. Just place it in your layout to get a material divider.
 */
public class Divider extends View {
    public Divider(Context context) {
        super(context, null, R.attr.carbon_dividerStyle);
        initDivider(null, R.attr.carbon_dividerStyle);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_dividerStyle);
        initDivider(attrs, R.attr.carbon_dividerStyle);
    }

    public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDivider(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Divider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDivider(attrs, defStyleAttr);
    }

    private void initDivider(AttributeSet attrs, int defStyleAttr) {
        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Divider, defStyleAttr, 0);
        int color = a.getColor(R.styleable.Divider_android_background, 0);
        setBackgroundColor(color);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) Carbon.getDip(getContext()));
    }


    // -------------------------------
    // transformations  // TODO: NineOldAndroids could be inlined here
    // -------------------------------

    public void setAlpha(float x) {
        ViewHelper.setAlpha(this, x);
    }

    public void setTranslationX(float x) {
        ViewHelper.setTranslationX(this, x);
    }

    public void setTranslationY(float y) {
        ViewHelper.setTranslationY(this, y);
    }

    public void setX(float x) {
        ViewHelper.setX(this, x);
    }

    public void setY(float y) {
        ViewHelper.setY(this, y);
    }

    public void setScaleX(float x) {
        ViewHelper.setScaleX(this, x);
    }

    public void setScaleY(float y) {
        ViewHelper.setScaleY(this, y);
    }

    public void setScrollX(int x) {
        ViewHelper.setScrollX(this, x);
    }

    public void setScrollY(int y) {
        ViewHelper.setScrollY(this, y);
    }

    public void setPivotX(float x) {
        ViewHelper.setPivotX(this, x);
    }

    public void setPivotY(float y) {
        ViewHelper.setPivotY(this, y);
    }

    public void setRotationX(float x) {
        ViewHelper.setRotationX(this, x);
    }

    public void setRotationY(float y) {
        ViewHelper.setRotationY(this, y);
    }

    public void setRotation(float y) {
        ViewHelper.setRotation(this, y);
    }
}
