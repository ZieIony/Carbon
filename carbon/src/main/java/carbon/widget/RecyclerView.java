package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.EdgeEffectCompat;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.drawable.TintPrimaryColorStateList;
import carbon.internal.ElevationComparator;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2015-04-28.
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView implements TintedView {

    private Field mLeftGlowField;
    private Field mRightGlowField;
    private Field mTopGlowField;
    private Field mBottomGlowField;
    private EdgeEffectCompat leftGlow;
    private EdgeEffectCompat rightGlow;
    private EdgeEffectCompat topGlow;
    private EdgeEffectCompat bottomGlow;
    private boolean clipToPadding;

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Carbon.initTint(this, attrs, defStyleAttr);

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
        if (tint != null)
            leftGlow.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
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
        if (tint != null)
            rightGlow.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
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
        if (tint != null)
            topGlow.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
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
        if (tint != null)
            bottomGlow.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
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

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateTint();
    }


    List<View> views;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    @Override
    protected void dispatchDraw(Canvas canvas) {
        views = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i++)
            views.add(getChildAt(i));
        Collections.sort(views, new ElevationComparator());

        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!child.isShown())
            return super.drawChild(canvas, child, drawingTime);

        if (!isInEditMode() && child instanceof ShadowView && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            ShadowView shadowView = (ShadowView) child;
            Shadow shadow = shadowView.getShadow();
            if (shadow != null) {
                paint.setAlpha((int) (ShadowGenerator.ALPHA * ViewHelper.getAlpha(child)));

                float childElevation = shadowView.getElevation() + shadowView.getTranslationZ();

                float[] childLocation = new float[]{(child.getLeft() + child.getRight()) / 2, (child.getTop() + child.getBottom()) / 2};
                Matrix matrix = carbon.internal.ViewHelper.getMatrix(child);
                matrix.mapPoints(childLocation);

                int[] location = new int[2];
                getLocationOnScreen(location);
                float x = childLocation[0] + location[0];
                float y = childLocation[1] + location[1];
                x -= getRootView().getWidth() / 2;
                y += getRootView().getHeight() / 2;   // looks nice
                float length = (float) Math.sqrt(x * x + y * y);

                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(
                        x / length * childElevation / 2,
                        y / length * childElevation / 2);
                canvas.translate(
                        child.getLeft(),
                        child.getTop());

                canvas.concat(matrix);
                canvas.scale(ShadowGenerator.SHADOW_SCALE, ShadowGenerator.SHADOW_SCALE);
                shadow.draw(canvas, child, paint);
                canvas.restoreToCount(saveCount);
            }
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(
                        child.getLeft(),
                        child.getTop());
                rippleDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }

        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int child) {
        return views != null ? indexOfChild(views.get(child)) : child;
    }


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;

    @Override
    public void setTint(ColorStateList list) {
        this.tint = list == null ? new TintPrimaryColorStateList(getContext()) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        if (tint == null)
            return;
        int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
        if (leftGlow != null)
            leftGlow.setColor(color);
        if (rightGlow != null)
            rightGlow.setColor(color);
        if (topGlow != null)
            topGlow.setColor(color);
        if (bottomGlow != null)
            bottomGlow.setColor(color);
    }
}
