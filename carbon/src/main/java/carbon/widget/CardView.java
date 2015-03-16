package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

/**
 * Created by Marcin on 2015-02-12.
 */
public class CardView extends android.widget.LinearLayout {
    private LinearLayout content;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_cardViewStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.carbon_cardview, this);
        content = (LinearLayout) findViewById(R.id.carbon_cardContent);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardView, defStyle, 0);
        content.setElevation(a.getDimension(R.styleable.CardView_carbon_elevation, 0));
        int color = a.getColor(R.styleable.CardView_android_background, 0);
        setBackgroundColor(color);
        a.recycle();

        super.setBackgroundDrawable(null);
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
    public void setBackgroundColor(int color) {
        content.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        content.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        content.setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        content.setBackgroundDrawable(background);
    }

    @Override
    public Drawable getBackground() {
        return content.getBackground();
    }

    @Override
    public float getElevation() {
        return content.getElevation();
    }

    @Override
    public synchronized void setElevation(float elevation) {
        content.setElevation(elevation);
    }

    @Override
    public float getTranslationZ() {
        return content.getTranslationZ();
    }

    @Override
    public void setTranslationZ(float translationZ) {
        content.setTranslationZ(translationZ);
    }

    public boolean isRect() {
        return content.isRect();
    }

    public void setRect(boolean rect) {
        content.setRect(rect);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        content.setTranslationZ(enabled ? 0 : -content.getElevation());
    }
}
