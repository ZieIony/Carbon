package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
import carbon.drawable.RectDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
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
        super(context, null, R.attr.carbon_recyclerViewStyle);
        initRecycler(null, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_recyclerViewStyle);
        initRecycler(attrs, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecycler(attrs, defStyleAttr);
    }

    private void initRecycler(AttributeSet attrs, int defStyleAttr) {
        if(attrs!=null) {
            Carbon.initTint(this, attrs, defStyleAttr);

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, 0);
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.RecyclerView_carbon_headerTint) {
                    setHeaderTint(a.getColor(attr, 0));
                } else if (attr == R.styleable.RecyclerView_carbon_headerMinHeight) {
                    setHeaderMinHeight((int) a.getDimension(attr, 0.0f));
                } else if (attr == R.styleable.RecyclerView_carbon_headerParallax) {
                    setHeaderParallax(a.getFloat(attr, 0.0f));
                } else if (attr == R.styleable.RecyclerView_android_divider) {
                    setDivider(a.getDrawable(attr), (int) a.getDimension(R.styleable.RecyclerView_android_dividerHeight, 0));
                }
            }
            a.recycle();
        }

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

        setClipToPadding(false);
    }

    public void setDivider(Drawable divider, int height) {
        addItemDecoration(new DividerItemDecoration(divider, height));
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
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
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
    protected void dispatchDraw(@NonNull Canvas canvas) {
        views = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++)
            views.add(getChildAt(i));
        Collections.sort(views, new ElevationComparator());

        dispatchDrawWithHeader(canvas);
    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
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
        this.tint = list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        if (color == 0) {
            setTint(Carbon.getThemeColor(getContext(), R.attr.colorPrimary));
        } else {
            setTint(ColorStateList.valueOf(color));
        }
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


    // -------------------------------
    // scroll bars
    // -------------------------------

    Drawable scrollBarDrawable;

    protected void onDrawHorizontalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        if (scrollBarDrawable == null) {
            Class<? extends Drawable> scrollBarClass = scrollBar.getClass();
            try {
                Field mVerticalThumbField = scrollBarClass.getDeclaredField("mHorizontalThumb");
                mVerticalThumbField.setAccessible(true);
                scrollBarDrawable= new RectDrawable(Carbon.getThemeColor(getContext(), R.attr.colorPrimary));
                mVerticalThumbField.set(scrollBar, scrollBarDrawable);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        if (scrollBarDrawable == null) {
            Class<? extends Drawable> scrollBarClass = scrollBar.getClass();
            try {
                Field mVerticalThumbField = scrollBarClass.getDeclaredField("mVerticalThumb");
                mVerticalThumbField.setAccessible(true);
                scrollBarDrawable = new RectDrawable(Carbon.getThemeColor(getContext(), R.attr.colorPrimary));
                mVerticalThumbField.set(scrollBar, scrollBarDrawable);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }


    // -------------------------------
    // header (do not copy)
    // -------------------------------

    View header;
    private float parallax = 0.5f;
    private int headerPadding = 0;
    private int headerTint = 0;
    private int minHeader = 0;

    protected void dispatchDrawWithHeader(Canvas canvas) {
        if (header != null) {
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG | Canvas.MATRIX_SAVE_FLAG);
            int headerHeight = header.getMeasuredHeight();
            float scroll = computeVerticalScrollOffset();
            canvas.clipRect(0, 0, getWidth(), Math.max(minHeader, headerHeight - scroll));
            canvas.translate(0, -scroll * parallax);
            header.draw(canvas);

            if (headerTint != 0) {
                paint.setColor(headerTint);
                paint.setAlpha((int) (Color.alpha(headerTint) * Math.min(headerHeight - minHeader, scroll) / (headerHeight - minHeader)));
                canvas.drawRect(0, 0, getWidth(), Math.max(minHeader + scroll, headerHeight), paint);
            }
            canvas.restoreToCount(saveCount);

            saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipRect(0, Math.max(minHeader, headerHeight - scroll), getWidth(), Integer.MAX_VALUE);
            super.dispatchDraw(canvas);
            canvas.restoreToCount(saveCount);
        } else {
            super.dispatchDraw(canvas);
        }
    }

    public View getHeader() {
        return header;
    }

    public void setHeader(View view) {
        header = view;
        view.setLayoutParams(generateDefaultLayoutParams());
        requestLayout();
    }

    public void setHeader(int resId) {
        header = LayoutInflater.from(getContext()).inflate(resId, this, false);
        requestLayout();
    }

    public float getHeaderParallax() {
        return parallax;
    }

    public void setHeaderParallax(float amount) {
        parallax = amount;
    }

    public int getHeaderTint() {
        return headerTint;
    }

    public void setHeaderTint(int color) {
        headerTint = color;
    }

    public int getHeaderMinHeight() {
        return minHeader;
    }

    public void setHeaderMinHeight(int height) {
        minHeader = height;
    }

    /**
     * @return parallax amount to the header applied when scrolling
     * @deprecated Naming convention change. Use {@link #getHeaderParallax()} instead
     */
    @Deprecated
    public float getParallax() {
        return parallax;
    }

    /**
     * @param amount parallax amount to apply to the header
     * @deprecated Naming convention change. Use {@link #setHeaderParallax(float)} instead
     */
    @Deprecated
    public void setParallax(float amount) {
        parallax = amount;
    }

    /**
     * @return min header height
     * @deprecated Naming convention change. Use {@link #getHeaderMinHeight()} instead
     */
    @Deprecated
    public int getMinHeaderHeight() {
        return minHeader;
    }

    /**
     * @param height min header height
     * @deprecated Naming convention change. Use {@link #setHeaderMinHeight(int)} instead
     */
    @Deprecated
    public void setMinHeaderHeight(int height) {
        minHeader = height;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingTop = getPaddingTop() - headerPadding;
        if (header != null) {
            measureChildWithMargins(header, widthMeasureSpec, 0, heightMeasureSpec, 0);
            headerPadding = header.getMeasuredHeight();
        } else {
            headerPadding = 0;
        }
        setPadding(getPaddingLeft(), paddingTop + headerPadding, getPaddingRight(), getPaddingBottom());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (header != null)
            header.layout(0, 0, getWidth(), header.getMeasuredHeight());
    }

    public static abstract class Adapter<VH extends ViewHolder, I> extends android.support.v7.widget.RecyclerView.Adapter<VH> {
        OnItemClickedListener onItemClickedListener;

        public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
            this.onItemClickedListener = onItemClickedListener;
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickedListener != null)
                        onItemClickedListener.onItemClicked(position);
                }
            });
        }

        public abstract I getItem(int position);
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable drawable;
        private int height;

        public DividerItemDecoration(Drawable drawable, int height) {
            this.drawable = drawable;
            this.height = height;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, android.support.v7.widget.RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (drawable == null)
                return;
            if (parent.getChildPosition(view) < 1)
                return;

            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = height;
            } else {
                outRect.left = height;
            }
        }

        @Override
        public void onDrawOver(Canvas c, android.support.v7.widget.RecyclerView parent, State state) {
            if (drawable == null) {
                super.onDrawOver(c, parent, state);
                return;
            }

            // Initialization needed to avoid compiler warning
            int left = 0, right = 0, top = 0, bottom = 0;
            int orientation = getOrientation(parent);
            int childCount = parent.getChildCount();

            if (orientation == LinearLayoutManager.VERTICAL) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
            } else { //horizontal
                top = parent.getPaddingTop();
                bottom = parent.getHeight() - parent.getPaddingBottom();
            }

            for (int i = 1; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                if (orientation == LinearLayoutManager.VERTICAL) {
                    bottom = child.getTop() - params.topMargin;
                    top = bottom - height;
                } else { //horizontal
                    right = child.getLeft() - params.leftMargin;
                    left = right - height;
                }
                c.save(Canvas.CLIP_SAVE_FLAG);
                c.clipRect(left, top, right, bottom);
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
                c.restore();
            }
        }

        private int getOrientation(android.support.v7.widget.RecyclerView parent) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                return layoutManager.getOrientation();
            } else {
                throw new IllegalStateException(
                        "DividerItemDecoration can only be used with a LinearLayoutManager.");
            }
        }
    }
}
