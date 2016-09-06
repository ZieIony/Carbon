package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.EdgeEffect;
import carbon.drawable.RectDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.DefaultItemAnimator;
import carbon.internal.ElevationComparator;
import carbon.internal.MatrixHelper;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowView;

import static com.nineoldandroids.view.animation.AnimatorProxy.NEEDS_PROXY;
import static com.nineoldandroids.view.animation.AnimatorProxy.wrap;

/**
 * Created by Marcin on 2015-04-28.
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView implements TintedView {

    private EdgeEffect leftGlow;
    private EdgeEffect rightGlow;
    private int mTouchSlop;
    EdgeEffect topGlow;
    EdgeEffect bottomGlow;
    private boolean drag = true;
    private float prevY;
    private int overscrollMode;
    private boolean clipToPadding;
    long prevScroll = 0;
    private boolean childDrawingOrderCallbackSet = false;
    OnItemClickedListener onItemClickedListener;

    public RecyclerView(Context context) {
        super(context, null, R.attr.carbon_recyclerViewStyle);
        initRecycler(null, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.RecyclerView, R.attr.carbon_recyclerViewStyle, R.styleable.RecyclerView_carbon_theme), attrs, R.attr.carbon_recyclerViewStyle);
        initRecycler(attrs, R.attr.carbon_recyclerViewStyle);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.RecyclerView, defStyleAttr, R.styleable.RecyclerView_carbon_theme), attrs, defStyleAttr);
        initRecycler(attrs, defStyleAttr);
    }

    private static int[] tintIds = new int[]{
            R.styleable.RecyclerView_carbon_tint,
            R.styleable.RecyclerView_carbon_tintMode,
            R.styleable.RecyclerView_carbon_backgroundTint,
            R.styleable.RecyclerView_carbon_backgroundTintMode,
            R.styleable.RecyclerView_carbon_animateColorChanges
    };

    private void initRecycler(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, R.style.carbon_RecyclerView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RecyclerView_carbon_overScroll) {
                setOverScrollMode(a.getInt(attr, ViewCompat.OVER_SCROLL_ALWAYS));
            } else if (attr == R.styleable.RecyclerView_carbon_headerTint) {
                setHeaderTint(a.getColor(attr, 0));
            } else if (attr == R.styleable.RecyclerView_carbon_headerMinHeight) {
                setHeaderMinHeight((int) a.getDimension(attr, 0.0f));
            } else if (attr == R.styleable.RecyclerView_carbon_headerParallax) {
                setHeaderParallax(a.getFloat(attr, 0.0f));
            } else if (attr == R.styleable.RecyclerView_android_divider) {
                Drawable drawable = a.getDrawable(attr);
                float height = a.getDimension(R.styleable.RecyclerView_android_dividerHeight, 0);
                if (drawable != null && height > 0)
                    setDivider(drawable, (int) height);
            }
        }

        Carbon.initTint(this, a, tintIds);

        a.recycle();

        setClipToPadding(false);
        setItemAnimator(new DefaultItemAnimator());

        initScrollbars();
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }

    public void addView(final View child, int index) {
        if (onItemClickedListener != null) {
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.onItemClicked(findContainingViewHolder(child).getAdapterPosition());
                }
            });
        }
        super.addView(child, index);
    }

    @Override
    public void removeView(View view) {
        if (onItemClickedListener != null)
            view.setOnClickListener(null);
        super.removeView(view);
    }

    @Override
    public void removeViewAt(int index) {
        final View child = getChildAt(index);
        if (child != null) {
            if (onItemClickedListener != null)
                child.setOnClickListener(null);
            super.removeView(child);
        }
    }

    public void setDivider(Drawable divider, int height) {
        addItemDecoration(new DividerItemDecoration(divider, height));
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.clipToPadding = clipToPadding;
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaY = prevY - ev.getY();

                if (!drag && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    drag = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (drag) {
                    final int oldY = computeVerticalScrollOffset();
                    int range = computeVerticalScrollRange() - getHeight();
                    if (header != null)
                        range += header.getHeight();
                    boolean canOverscroll = overscrollMode == ViewCompat.OVER_SCROLL_ALWAYS ||
                            (overscrollMode == ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    if (canOverscroll) {
                        float pulledToY = oldY + deltaY;
                        if (pulledToY < 0) {
                            topGlow.onPull(deltaY / getHeight(), ev.getX() / getWidth());
                            if (!bottomGlow.isFinished())
                                bottomGlow.onRelease();
                        } else if (pulledToY > range) {
                            bottomGlow.onPull(deltaY / getHeight(), 1.f - ev.getX() / getWidth());
                            if (!topGlow.isFinished())
                                topGlow.onRelease();
                        }
                        if (topGlow != null && (!topGlow.isFinished() || !bottomGlow.isFinished()))
                            postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (drag) {
                    drag = false;

                    if (topGlow != null) {
                        topGlow.onRelease();
                        bottomGlow.onRelease();
                    }
                }
                break;
        }
        prevY = ev.getY();

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (drag || topGlow == null)
            return;
        int range = computeVerticalScrollRange() - getHeight();
        if (header != null)
            range += header.getHeight();
        boolean canOverscroll = overscrollMode == ViewCompat.OVER_SCROLL_ALWAYS ||
                (overscrollMode == ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

        if (canOverscroll) {
            long t = System.currentTimeMillis();
            /*int velx = (int) (dx * 1000.0f / (t - prevScroll));
            if (computeHorizontalScrollOffset() == 0 && dx < 0) {
                leftGlow.onAbsorb(-velx);
            } else if (computeHorizontalScrollOffset() == computeHorizontalScrollRange() - getWidth() && dx > 0) {
                rightGlow.onAbsorb(velx);
            }*/
            int vely = (int) (dy * 1000.0f / (t - prevScroll));
            if (computeVerticalScrollOffset() == 0 && dy < 0) {
                topGlow.onAbsorb(-vely);
            } else if (computeVerticalScrollOffset() == range && dy > 0) {
                bottomGlow.onAbsorb(vely);
            }
            prevScroll = t;
        }
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
    public void setOverScrollMode(int mode) {
        if (mode != OVER_SCROLL_NEVER) {
            if (topGlow == null) {
                Context context = getContext();
                topGlow = new EdgeEffect(context);
                bottomGlow = new EdgeEffect(context);
                updateTint();
            }
        } else {
            topGlow = null;
            bottomGlow = null;
        }
        try {
            super.setOverScrollMode(ViewCompat.OVER_SCROLL_NEVER);
        } catch (Exception e) {
            // Froyo
        }
        this.overscrollMode = mode;
    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!isInEditMode() && child instanceof ShadowView && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            ShadowView shadowView = (ShadowView) child;
            Shadow shadow = shadowView.getShadow();
            if (shadow != null) {
                paint.setAlpha((int) (ShadowGenerator.ALPHA * ViewHelper.getAlpha(child)));

                float childElevation = shadowView.getElevation() + shadowView.getTranslationZ();

                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(0, childElevation / 2);
                canvas.translate(child.getLeft(), child.getTop());

                Matrix matrix = MatrixHelper.getMatrix(child);
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
        if (childDrawingOrderCallbackSet)
            return super.getChildDrawingOrder(childCount, child);
        return views != null ? indexOfChild(views.get(child)) : child;
    }

    @Override
    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        super.setChildDrawingOrderCallback(childDrawingOrderCallback);
        childDrawingOrderCallbackSet = childDrawingOrderCallback != null;
    }

    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            updateTint();
            ViewCompat.postInvalidateOnAnimation(RecyclerView.this);
        }
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            updateBackgroundTint();
            ViewCompat.postInvalidateOnAnimation(RecyclerView.this);
        }
    };

    @Override
    public void setTint(ColorStateList list) {
        this.tint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        if (color == 0) {
            setTint(new DefaultPrimaryColorStateList(getContext()));
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
        scrollBarDrawable = null;
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        this.tintMode = mode;
        updateTint();
    }

    @Override
    public PorterDuff.Mode getTintMode() {
        return tintMode;
    }

    @Override
    public void setBackgroundTint(ColorStateList list) {
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        if (color == 0) {
            setBackgroundTint(new DefaultPrimaryColorStateList(getContext()));
        } else {
            setBackgroundTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        if (getBackground() == null)
            return;
        if (backgroundTint != null && backgroundTintMode != null) {
            int color = backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor());
            getBackground().setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            getBackground().setColorFilter(null);
        }
    }

    @Override
    public void setBackgroundTintMode(@NonNull PorterDuff.Mode mode) {
        this.backgroundTintMode = mode;
        updateBackgroundTint();
    }

    @Override
    public PorterDuff.Mode getBackgroundTintMode() {
        return backgroundTintMode;
    }

    public boolean isAnimateColorChangesEnabled() {
        return animateColorChanges;
    }

    public void setAnimateColorChangesEnabled(boolean animateColorChanges) {
        this.animateColorChanges = animateColorChanges;
        if (tint != null && !(tint instanceof AnimatedColorStateList))
            setTint(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTint(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
    }


    // -------------------------------
    // scroll bars
    // -------------------------------

    Drawable scrollBarDrawable;

    private void initScrollbars() {
        try {
            Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
            mScrollCacheField.setAccessible(true);
            Object mScrollCache = mScrollCacheField.get(this);

            if (mScrollCache == null)
                return;

            Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollBar = scrollBarField.get(mScrollCache);

            Field mVerticalThumbField = scrollBar.getClass().getDeclaredField("mVerticalThumb");
            mVerticalThumbField.setAccessible(true);
            scrollBarDrawable = new RectDrawable(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE);
            mVerticalThumbField.set(scrollBar, scrollBarDrawable);

            Field mHorizontalThumbField = scrollBar.getClass().getDeclaredField("mHorizontalThumb");
            mHorizontalThumbField.setAccessible(true);
            scrollBarDrawable = new RectDrawable(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE);
            mHorizontalThumbField.set(scrollBar, scrollBarDrawable);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        if (topGlow != null) {
            final int scrollY = computeVerticalScrollOffset();
            if (!topGlow.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();

                canvas.translate(getPaddingLeft(), Math.min(0, scrollY));
                topGlow.setSize(width, getHeight());
                if (topGlow.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!bottomGlow.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = getWidth() - getPaddingLeft() - getPaddingRight();
                final int height = getHeight();

                canvas.translate(-width + getPaddingLeft(),
                        height);
                canvas.rotate(180, width, 0);
                bottomGlow.setSize(width, height);
                if (bottomGlow.draw(canvas)) {
                    postInvalidate();
                }
                canvas.restoreToCount(restoreCount);
            }
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

    public static abstract class ListAdapter<VH extends ViewHolder, I> extends android.support.v7.widget.RecyclerView.Adapter<VH> {
        public ListAdapter() {
            items = new ArrayList<>();
        }

        public ListAdapter(List<I> items) {
            this.items = items;
        }

        protected List<I> items;

        public I getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(@NonNull List<I> items) {
            this.items = items;
        }

        public List<I> getItems() {
            return items;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    public static abstract class ArrayAdapter<VH extends ViewHolder, I> extends android.support.v7.widget.RecyclerView.Adapter<VH> {
        public ArrayAdapter() {
            items = (I[]) new Object[0];    // doesn't really matter
        }

        public ArrayAdapter(I[] items) {
            this.items = items;
        }

        protected I[] items;

        public I getItem(int position) {
            return items[position];
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        public void setItems(@NonNull I[] items) {
            this.items = items;
        }

        public I[] getItems() {
            return items;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

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
                    bottom = (int) (child.getTop() - params.topMargin + ViewHelper.getTranslationY(child));
                    top = bottom - height;
                } else { //horizontal
                    right = (int) (child.getLeft() - params.leftMargin + ViewHelper.getTranslationX(child));
                    left = right - height;
                }
                c.save(Canvas.CLIP_SAVE_FLAG);
                c.clipRect(left, top, right, bottom);
                drawable.setAlpha((int) (ViewHelper.getAlpha(child) * 255));
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


    // -------------------------------
    // transformations
    // -------------------------------

    public float getAlpha() {
        return NEEDS_PROXY ? wrap(this).getAlpha() : super.getAlpha();
    }

    public void setAlpha(float alpha) {
        if (NEEDS_PROXY) {
            wrap(this).setAlpha(alpha);
        } else {
            super.setAlpha(alpha);
        }
    }

    public float getPivotX() {
        return NEEDS_PROXY ? wrap(this).getPivotX() : super.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotX(pivotX);
        } else {
            super.setPivotX(pivotX);
        }
    }

    public float getPivotY() {
        return NEEDS_PROXY ? wrap(this).getPivotY() : super.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotY(pivotY);
        } else {
            super.setPivotY(pivotY);
        }
    }

    public float getRotation() {
        return NEEDS_PROXY ? wrap(this).getRotation() : super.getRotation();
    }

    public void setRotation(float rotation) {
        if (NEEDS_PROXY) {
            wrap(this).setRotation(rotation);
        } else {
            super.setRotation(rotation);
        }
    }

    public float getRotationX() {
        return NEEDS_PROXY ? wrap(this).getRotationX() : super.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationX(rotationX);
        } else {
            super.setRotationX(rotationX);
        }
    }

    public float getRotationY() {
        return NEEDS_PROXY ? wrap(this).getRotationY() : super.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationY(rotationY);
        } else {
            super.setRotationY(rotationY);
        }
    }

    public float getScaleX() {
        return NEEDS_PROXY ? wrap(this).getScaleX() : super.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleX(scaleX);
        } else {
            super.setScaleX(scaleX);
        }
    }

    public float getScaleY() {
        return NEEDS_PROXY ? wrap(this).getScaleY() : super.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleY(scaleY);
        } else {
            super.setScaleY(scaleY);
        }
    }

    public float getTranslationX() {
        return NEEDS_PROXY ? wrap(this).getTranslationX() : super.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationX(translationX);
        } else {
            super.setTranslationX(translationX);
        }
    }

    public float getTranslationY() {
        return NEEDS_PROXY ? wrap(this).getTranslationY() : super.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationY(translationY);
        } else {
            super.setTranslationY(translationY);
        }
    }

    public float getX() {
        return NEEDS_PROXY ? wrap(this).getX() : super.getX();
    }

    public void setX(float x) {
        if (NEEDS_PROXY) {
            wrap(this).setX(x);
        } else {
            super.setX(x);
        }
    }

    public float getY() {
        return NEEDS_PROXY ? wrap(this).getY() : super.getY();
    }

    public void setY(float y) {
        if (NEEDS_PROXY) {
            wrap(this).setY(y);
        } else {
            super.setY(y);
        }
    }
}
