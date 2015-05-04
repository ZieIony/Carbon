package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

import carbon.R;
import carbon.drawable.EdgeEffectCompat;

/**
 * Created by Marcin on 2015-04-28.
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView {

    private Field mLeftGlowField;
    private Field mRightGlowField;
    private Field mTopGlowField;
    private Field mBottomGlowField;
    private EdgeEffectCompat leftGlow;
    private EdgeEffectCompat rightGlow;
    private EdgeEffectCompat topGlow;
    private EdgeEffectCompat bottomGlow;
    private boolean clipToPadding;
    private int edgeEffectColor;

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RecyclerView_carbon_edgeEffectColor) {
                setEdgeEffectColor(a.getColor(attr, 0));
            }
        }
        a.recycle();

        Class klass = android.support.v7.widget.RecyclerView.class;
        try {
            mLeftGlowField = klass.getDeclaredField("mLeftGlow");
            mLeftGlowField.setAccessible(true);
            mRightGlowField = klass.getDeclaredField("mRightGlow");
            mRightGlowField.setAccessible(true);
            mTopGlowField = klass.getDeclaredField("mTopGlow");
            mTopGlowField.setAccessible(true);
            mBottomGlowField = klass.getDeclaredField("mBottomGlow");
            mBottomGlowField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    void ensureLeftGlow() {
        if (leftGlow != null) {
            return;
        }
        leftGlow = new EdgeEffectCompat(getContext());
        leftGlow.setColor(edgeEffectColor);
        try {
            mLeftGlowField.set(this, leftGlow);
        } catch (IllegalAccessException e) {
        }
        if (clipToPadding) {
            leftGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                    getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        } else {
            leftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
        }
    }

    void ensureRightGlow() {
        if (rightGlow != null) {
            return;
        }
        rightGlow = new EdgeEffectCompat(getContext());
        rightGlow.setColor(edgeEffectColor);
        try {
            mRightGlowField.set(this, rightGlow);
        } catch (IllegalAccessException e) {
        }
        if (clipToPadding) {
            rightGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                    getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        } else {
            rightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
        }
    }

    void ensureTopGlow() {
        if (topGlow != null) {
            return;
        }
        topGlow = new EdgeEffectCompat(getContext());
        topGlow.setColor(edgeEffectColor);
        try {
            mTopGlowField.set(this, topGlow);
        } catch (IllegalAccessException e) {
        }
        if (clipToPadding) {
            topGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
        } else {
            topGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
        }

    }

    void ensureBottomGlow() {
        if (bottomGlow != null) {
            return;
        }
        bottomGlow = new EdgeEffectCompat(getContext());
        bottomGlow.setColor(edgeEffectColor);
        try {
            mBottomGlowField.set(this, bottomGlow);
        } catch (IllegalAccessException e) {
        }
        if (clipToPadding) {
            bottomGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
        } else {
            bottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    void invalidateGlows() {
        leftGlow = rightGlow = topGlow = bottomGlow = null;
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.clipToPadding = clipToPadding;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ensureTopGlow();
        ensureLeftGlow();
        ensureRightGlow();
        ensureBottomGlow();
        leftGlow.setDisplacement(1 - ev.getY() / getHeight());
        rightGlow.setDisplacement(ev.getY() / getHeight());
        topGlow.setDisplacement(ev.getX() / getWidth());
        bottomGlow.setDisplacement(1 - ev.getX() / getWidth());
        return super.dispatchTouchEvent(ev);
    }

    public void setEdgeEffectColor(int edgeEffectColor) {
        this.edgeEffectColor = edgeEffectColor;
        if (leftGlow != null)
            leftGlow.setColor(edgeEffectColor);
        if (rightGlow != null)
            rightGlow.setColor(edgeEffectColor);
        if (topGlow != null)
            topGlow.setColor(edgeEffectColor);
        if (bottomGlow != null)
            bottomGlow.setColor(edgeEffectColor);
    }
}
