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
public class CardView extends LinearLayout {
    private LinearLayout content;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_cardViewStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        content = new LinearLayout(getContext());
        content.setOrientation(VERTICAL);
        super.addView(content,-1,generateDefaultLayoutParams());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardView, defStyle, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CardView_carbon_cardElevation) {
                setCardElevation(a.getDimension(attr, 0));
            } else if (attr == R.styleable.CardView_carbon_cardBackground) {
                setCardBackgroundDrawable(a.getDrawable(attr));
            } else if (attr == R.styleable.CardView_carbon_cardPadding) {
                int padding = (int) a.getDimension(attr, 0);
                setCardPadding(padding, padding, padding, padding);
            } else if (attr == R.styleable.CardView_android_padding) {
                int padding = (int) a.getDimension(attr, 0);
                setPadding(padding, padding, padding, padding);
            } else if (attr == R.styleable.CardView_carbon_cardCornerRadius) {
                setCardCornerRadius(a.getDimension(attr, 0));
            }
        }
        a.recycle();
    }

    @Override
    public void addView(View child) {
        content.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        content.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        content.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        content.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        content.addView(child, width, height);
    }

    public LinearLayout getContent() {
        return content;
    }

    public void setCardBackgroundColor(int color) {
        content.setBackgroundColor(color);
    }

    public void setCardBackgroundResource(int resid) {
        content.setBackgroundResource(resid);
    }

    public void setCardBackground(Drawable background) {
        content.setBackgroundDrawable(background);
    }

    public void setCardBackgroundDrawable(Drawable background) {
        content.setBackgroundDrawable(background);
    }

    public void setCardCornerRadius(float cornerRadius) {
        content.setCornerRadius(cornerRadius);
    }

    public void setCardElevation(float elevation) {
        content.setElevation(elevation);
    }

    public void setCardPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        content.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }
}
