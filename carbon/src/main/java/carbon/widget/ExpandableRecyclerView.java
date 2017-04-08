package carbon.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.EdgeEffect;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.ElevationComparator;
import carbon.recycler.ArrayAdapter;
import carbon.shadow.ShadowView;

// TODO: make extend carbon.widget.RecyclerView
public class ExpandableRecyclerView extends android.support.v7.widget.RecyclerView implements TintedView, VisibleView {

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

    public ExpandableRecyclerView(Context context) {
        super(context, null, R.attr.carbon_recyclerViewStyle);
        initRecycler(null, R.attr.carbon_recyclerViewStyle);
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.RecyclerView, R.attr.carbon_recyclerViewStyle, R.styleable.RecyclerView_carbon_theme), attrs, R.attr.carbon_recyclerViewStyle);
        initRecycler(attrs, R.attr.carbon_recyclerViewStyle);
    }

    public ExpandableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setWillNotDraw(false);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }

    public void addView(final View child, int index) {
        if (onItemClickedListener != null) {
            child.setOnClickListener(v -> onItemClickedListener.onItemClicked(findContainingViewHolder(child).getAdapterPosition()));
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
        if (header != null && (getChildCount() == 0 || getChildAt(0).getTop() + getScrollY() > ev.getY()))
            if (header.dispatchTouchEvent(ev))
                return true;

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
        super.setOverScrollMode(OVER_SCROLL_NEVER);
        this.overscrollMode = mode;
    }

    RectF childRect = new RectF();

    @Override
    public boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        // TODO: why isShown() returns false after being reattached?
        if (child instanceof ShadowView && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH || ((ShadowView) child).getElevationShadowColor() != null)) {
            ShadowView shadowView = (ShadowView) child;
            shadowView.drawShadow(canvas);
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.translate(child.getLeft(), child.getTop());
                canvas.concat(child.getMatrix());
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

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        if (getAdapter() != null)
            ss.stateToSave = ((ExpandableRecyclerView.Adapter) this.getAdapter()).getExpandedGroups();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        if (getAdapter() != null)
            ((ExpandableRecyclerView.Adapter) getAdapter()).setExpandedGroups(ss.stateToSave);
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        SparseBooleanArray stateToSave;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(ExpandableRecyclerView.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.stateToSave = in.readSparseBooleanArray();
        }

        @Override
        public int describeContents() {
            return 0;
        }


        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeSparseBooleanArray(this.stateToSave);
        }

        public Parcelable getSuperState() {
            return superState;
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = animation -> {
        updateTint();
        ViewCompat.postInvalidateOnAnimation(ExpandableRecyclerView.this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(ExpandableRecyclerView.this);
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

    protected void onDrawHorizontalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setColorFilter(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE, tintMode);
        scrollBar.setBounds(l, t, r, b);
        scrollBar.draw(canvas);
    }

    protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b) {
        scrollBar.setColorFilter(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE, tintMode);
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

    /**
     * @return parallax amount to the header applied when scrolling
     */
    public float getHeaderParallax() {
        return parallax;
    }

    /**
     * @param amount parallax amount to apply to the header
     */
    public void setHeaderParallax(float amount) {
        parallax = amount;
    }

    public int getHeaderTint() {
        return headerTint;
    }

    public void setHeaderTint(int color) {
        headerTint = color;
    }

    /**
     * @return min header height
     */
    public int getHeaderMinHeight() {
        return minHeader;
    }

    /**
     * @param height min header height
     */
    public void setHeaderMinHeight(int height) {
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
        if (getLayoutManager() == null)
            setLayoutManager(new LinearLayoutManager(getContext()));
        super.onLayout(changed, l, t, r, b);
        if (header != null)
            header.layout(0, 0, getWidth(), header.getMeasuredHeight());
    }

    @Override
    public void setAdapter(android.support.v7.widget.RecyclerView.Adapter adapter) {
        if (!(adapter instanceof ExpandableRecyclerView.Adapter))
            throw new IllegalArgumentException("adapter has to be of type ExpandableRecyclerView.Adapter");
        super.setAdapter(adapter);
    }

    public interface OnChildItemClickedListener {
        void onChildItemClicked(int group, int position);
    }

    public static abstract class Adapter<CVH extends ViewHolder, GVH extends ViewHolder, C, G> extends ArrayAdapter<ViewHolder, Object> {
        private static final int TYPE_HEADER = 0;

        SparseBooleanArray expanded = new SparseBooleanArray();
        private OnChildItemClickedListener onChildItemClickedListener;

        public Adapter() {
        }

        boolean isExpanded(int group) {
            return expanded.get(group);
        }

        SparseBooleanArray getExpandedGroups() {
            return expanded;
        }

        public void setExpandedGroups(SparseBooleanArray expanded) {
            this.expanded = expanded;
        }

        public void expand(int group) {
            if (isExpanded(group))
                return;
            int position = 0;
            for (int i = 0; i < group; i++) {
                position++;
                if (isExpanded(i))
                    position += getChildItemCount(i);
            }
            position++;
            notifyItemRangeInserted(position, getChildItemCount(group));
            expanded.put(group, true);
        }

        public void collapse(int group) {
            if (!isExpanded(group))
                return;
            int position = 0;
            for (int i = 0; i < group; i++) {
                position++;
                if (isExpanded(i))
                    position += getChildItemCount(i);
            }
            position++;
            notifyItemRangeRemoved(position, getChildItemCount(group));
            expanded.put(group, false);
        }

        public abstract int getGroupItemCount();

        public abstract int getChildItemCount(int group);

        @Override
        public int getItemCount() {
            int count = 0;
            for (int i = 0; i < getGroupItemCount(); i++) {
                count += isExpanded(i) ? getChildItemCount(i) + 1 : 1;
            }
            return count;
        }

        public abstract G getGroupItem(int position);

        public abstract C getChildItem(int group, int position);

        @Override
        public Object getItem(int i) {
            int group = 0;
            while (group < getGroupItemCount()) {
                if (i > 0 && !isExpanded(group)) {
                    i--;
                    group++;
                    continue;
                }
                if (i > 0 && isExpanded(group)) {
                    i--;
                    if (i < getChildItemCount(group))
                        return getChildItem(group, i);
                    i -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (i == 0)
                    return getGroupItem(group);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            int group = 0;
            while (group < getGroupItemCount()) {
                if (i > 0 && !isExpanded(group)) {
                    i--;
                    group++;
                    continue;
                }
                if (i > 0 && isExpanded(group)) {
                    i--;
                    if (i < getChildItemCount(group)) {
                        onBindChildViewHolder((CVH) holder, group, i);
                        return;
                    }
                    i -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (i == 0) {
                    onBindGroupViewHolder((GVH) holder, group);
                    return;
                }
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return viewType == TYPE_HEADER ? onCreateGroupViewHolder(parent) : onCreateChildViewHolder(parent, viewType);
        }

        protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

        protected abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

        public abstract int getChildItemViewType(int group, int position);

        @Override
        public int getItemViewType(int i) {
            int group = 0;
            while (group < getGroupItemCount()) {
                if (i > 0 && !isExpanded(group)) {
                    i--;
                    group++;
                    continue;
                }
                if (i > 0 && isExpanded(group)) {
                    i--;
                    if (i < getChildItemCount(group))
                        return getChildItemViewType(group, i);
                    i -= getChildItemCount(group);
                    group++;
                    continue;
                }
                if (i == 0)
                    return TYPE_HEADER;
            }
            throw new IndexOutOfBoundsException();
        }

        public void setOnChildItemClickedListener(ExpandableRecyclerView.OnChildItemClickedListener onItemClickedListener) {
            this.onChildItemClickedListener = onItemClickedListener;
        }

        public void onBindChildViewHolder(CVH holder, final int group, final int position) {
            holder.itemView.setAlpha(1);
            holder.itemView.setOnClickListener(__ -> {
                if (Adapter.this.onChildItemClickedListener != null)
                    Adapter.this.onChildItemClickedListener.onChildItemClicked(group, position);
            });
        }

        public void onBindGroupViewHolder(final GVH holder, final int group) {
            if (holder instanceof GroupViewHolder)
                ((GroupViewHolder) holder).setExpanded(isExpanded(group));
            holder.itemView.setOnClickListener(__ -> {
                if (isExpanded(group)) {
                    collapse(group);
                    if (holder instanceof GroupViewHolder)
                        ((GroupViewHolder) holder).collapse();
                } else {
                    expand(group);
                    if (holder instanceof GroupViewHolder)
                        ((GroupViewHolder) holder).expand();
                }
            });
        }
    }

    public static abstract class GroupViewHolder extends RecyclerView.ViewHolder {

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void expand();

        public abstract void collapse();

        public abstract void setExpanded(boolean expanded);

        public abstract boolean isExpanded();
    }

    public static class SimpleGroupViewHolder extends GroupViewHolder {
        ImageView expandedIndicator;
        TextView text;
        private boolean expanded;

        public SimpleGroupViewHolder(Context context) {
            super(View.inflate(context, R.layout.carbon_expandablerecyclerview_group, null));
            itemView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            expandedIndicator = (ImageView) itemView.findViewById(R.id.carbon_groupExpandedIndicator);
            text = (TextView) itemView.findViewById(R.id.carbon_groupText);
        }

        public void expand() {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                expandedIndicator.setRotation(180 * (float) (animation.getAnimatedValue()));
                expandedIndicator.postInvalidate();
            });
            animator.start();
            expanded = true;
        }

        public void collapse() {
            ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                expandedIndicator.setRotation(180 * (float) (animation.getAnimatedValue()));
                expandedIndicator.postInvalidate();
            });
            animator.start();
            expanded = false;
        }

        public void setExpanded(boolean expanded) {
            expandedIndicator.setRotation(expanded ? 180 : 0);
            this.expanded = expanded;
        }

        @Override
        public boolean isExpanded() {
            return expanded;
        }

        public void setText(String t) {
            text.setText(t);
        }

        public String getText() {
            return text.getText().toString();
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
                    bottom = (int) (child.getTop() - params.topMargin + child.getTranslationY());
                    top = bottom - height;
                } else { //horizontal
                    right = (int) (child.getLeft() - params.leftMargin + child.getTranslationX());
                    left = right - height;
                }
                c.save(Canvas.CLIP_SAVE_FLAG);
                c.clipRect(left, top, right, bottom);
                drawable.setAlpha((int) (child.getAlpha() * 255));
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
