package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ValueAnimator;

import carbon.R;
import carbon.shadow.ShadowView;
import carbon.widget.TextView;

/**
 * Created by Marcin on 2014-12-13.
 */
public class Toolbar extends LinearLayout implements ShadowView {
    private float elevation = 0;
    private float translationZ = 0;
    private ViewGroup content;
    private boolean isRect = true;

    public Toolbar(Context context) {
        super(context);
        init(null, R.attr.carbon_toolbarStyle);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_toolbarStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.carbon_toolbar, this);
        content = (ViewGroup) findViewById(R.id.carbon_toolbarContent);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyle, 0);
        setElevation(a.getDimension(R.styleable.Toolbar_carbon_elevation, 0));
        int color = a.getColor(R.styleable.Toolbar_android_background, 0);
        setBackgroundColor(color);
        a.recycle();
    }

    @Override
    public void addView(View child) {
        if (content != null) {
            content.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (content != null) {
            content.addView(child, index);
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (content != null) {
            content.addView(child, width, height);
        } else {
            super.addView(child, width, height);
        }
    }

    @Override
    public float getElevation() {
        return elevation;
    }

    public synchronized void setElevation(float elevation) {
        elevation = Math.max(0,Math.min(elevation,25));
        if (elevation == this.elevation)
            return;
        this.elevation = elevation;
        if (getParent() != null)
            ((View) getParent()).invalidate();
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    private synchronized void setTranslationZInternal(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void setTranslationZ(float translationZ) {
        ValueAnimator animator = ValueAnimator.ofFloat(this.translationZ, translationZ);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationZInternal((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public boolean isRect() {
        return isRect;
    }

    @Override
    public void setRect(boolean rect) {
        this.isRect = rect;
    }

    public void setText(String text) {
        TextView title = (TextView) findViewById(R.id.carbon_toolbarTitle);
        if (text != null) {
            title.setText(text);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
    }

    public void setText(int resId){
        setText(getResources().getString(resId));
    }
}
