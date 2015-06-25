package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import carbon.R;
import carbon.drawable.VectorDrawable;

/**
 * Created by Marcin on 2014-12-02.
 */
public class SVGView extends ImageView {
    public SVGView(Context context) {
        this(context, null);
    }

    public SVGView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SVGView, defStyleAttr, 0);
        if (a.hasValue(R.styleable.SVGView_carbon_src))
            setImageDrawable(new VectorDrawable(getContext(), a.getResourceId(R.styleable.SVGView_carbon_src, 0)));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY && getDrawable() != null) {
            width = (int) (getDrawable().getBounds().width() * getResources().getDimension(R.dimen.carbon_1dip));
            width += getPaddingLeft() + getPaddingRight();
        }
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY && getDrawable() != null) {
            height = (int) (getDrawable().getBounds().height() * getResources().getDimension(R.dimen.carbon_1dip));
            height += getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void setImageResource(int resId) {
        try {
            super.setImageResource(resId);
        } catch (Exception e) {
            setImageDrawable(new VectorDrawable(getContext(), resId));
        }
    }
}
