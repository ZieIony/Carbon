package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.EdgeEffect;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.ElevationComparator;
import carbon.internal.RevealAnimator;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.SelectableItemsAdapter;
import carbon.view.MarginView;
import carbon.view.MaxSizeView;
import carbon.view.RevealView;
import carbon.view.SelectionMode;
import carbon.view.ShadowView;
import carbon.view.ShapeModelView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.TintedView;
import carbon.view.TouchMarginView;
import carbon.view.VisibleView;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView
        implements
        ShadowView,
        RippleView,
        TouchMarginView,
        StateAnimatorView,
        AnimatedView,
        ShapeModelView,
        TintedView,
        StrokeView,
        MaxSizeView,
        RevealView,
        VisibleView,
        MarginView {

    public interface OnItemClickedListener<Type> {
        void onItemClicked(View view, Type item, int position);
    }

    public interface OnItemClickedListener2<Type> {
        void onItemClicked(Type item);
    }

    public class EdgeEffectFactory extends androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory {
        @NonNull
        @Override
        protected android.widget.EdgeEffect createEdgeEffect(@NonNull androidx.recyclerview.widget.RecyclerView view, int direction) {
            EdgeEffect edgeEffect = new EdgeEffect(getContext());
            if (direction == DIRECTION_TOP) {
                topGlow = edgeEffect;
            } else if (direction == DIRECTION_BOTTOM) {
                bottomGlow = edgeEffect;
            } else if (direction == DIRECTION_LEFT) {
                leftGlow = edgeEffect;
            } else if (direction == DIRECTION_RIGHT) {
                rightGlow = edgeEffect;
            }
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            edgeEffect.setColor(color);
            return edgeEffect;
        }
    }

    private boolean childDrawingOrderCallbackSet = false;

    private int scrollX = 0;
    private int scrollY = 0;

    private EdgeEffect leftGlow, rightGlow, topGlow, bottomGlow;

    private OnTouchListener onDispatchTouchListener;

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

    private static int[] animationIds = new int[]{
            R.styleable.RecyclerView_carbon_inAnimation,
            R.styleable.RecyclerView_carbon_outAnimation
    };
    private static int[] tintIds = new int[]{
            R.styleable.RecyclerView_carbon_tint,
            R.styleable.RecyclerView_carbon_tintMode,
            R.styleable.RecyclerView_carbon_backgroundTint,
            R.styleable.RecyclerView_carbon_backgroundTintMode,
            R.styleable.RecyclerView_carbon_animateColorChanges
    };
    private static int[] strokeIds = new int[]{
            R.styleable.RecyclerView_carbon_stroke,
            R.styleable.RecyclerView_carbon_strokeWidth
    };
    private static int[] cornerCutRadiusIds = new int[]{
            R.styleable.RecyclerView_carbon_cornerRadiusTopStart,
            R.styleable.RecyclerView_carbon_cornerRadiusTopEnd,
            R.styleable.RecyclerView_carbon_cornerRadiusBottomStart,
            R.styleable.RecyclerView_carbon_cornerRadiusBottomEnd,
            R.styleable.RecyclerView_carbon_cornerRadius,
            R.styleable.RecyclerView_carbon_cornerCutTopStart,
            R.styleable.RecyclerView_carbon_cornerCutTopEnd,
            R.styleable.RecyclerView_carbon_cornerCutBottomStart,
            R.styleable.RecyclerView_carbon_cornerCutBottomEnd,
            R.styleable.RecyclerView_carbon_cornerCut
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.RecyclerView_carbon_maxWidth,
            R.styleable.RecyclerView_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.RecyclerView_carbon_elevation,
            R.styleable.RecyclerView_carbon_elevationShadowColor,
            R.styleable.RecyclerView_carbon_elevationAmbientShadowColor,
            R.styleable.RecyclerView_carbon_elevationSpotShadowColor
    };

    private void initRecycler(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, R.style.carbon_RecyclerView);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RecyclerView_carbon_overScroll) {
                setOverScrollMode(a.getInt(attr, OVER_SCROLL_ALWAYS));
            } else if (attr == R.styleable.RecyclerView_android_divider) {
                Drawable drawable = Carbon.getDefaultColorDrawable(this, a, attr);
                float height = a.getDimension(R.styleable.RecyclerView_android_dividerHeight, 0);
                if (drawable != null && height > 0) {
                    setDivider(drawable, (int) height);
                }
            }
        }

        Carbon.initDefaultBackground(this, a, R.styleable.RecyclerView_android_background);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initStroke(this, a, strokeIds);
        Carbon.initCornerCutRadius(this, a, cornerCutRadiusIds);

        a.recycle();

        setClipToPadding(false);
        setWillNotDraw(false);
        setEdgeEffectFactory(new EdgeEffectFactory());
    }

    public void setDivider(Drawable divider, int height) {
        DividerItemDecoration decoration = new DividerItemDecoration(divider, height);
        decoration.setDrawBefore(position -> position > 0);
        addItemDecoration(decoration);
    }

    @Override
    public void offsetChildrenHorizontal(int dx) {
        super.offsetChildrenHorizontal(dx);
        scrollX -= dx;
    }

    public int getListScrollX() {
        return scrollX;
    }

    public int getMaxScrollX() {
        Adapter adapter = getAdapter();
        LayoutManager layoutManager = getLayoutManager();
        if (adapter == null || layoutManager == null || getChildCount() == 0 || adapter.getItemCount() == 0)
            return 0;
        View view = getChildAt(getChildCount() - 1);
        int position = getChildAdapterPosition(view);
        if (position == adapter.getItemCount())
            return Math.max(0, view.getRight() - getWidth());
        return Integer.MAX_VALUE;
    }

    @Override
    public void offsetChildrenVertical(int dy) {
        super.offsetChildrenVertical(dy);
        scrollY -= dy;
    }

    public int getListScrollY() {
        return scrollY;
    }

    public int getMaxScrollY() {
        Adapter adapter = getAdapter();
        LayoutManager layoutManager = getLayoutManager();
        if (adapter == null || layoutManager == null || getChildCount() == 0 || adapter.getItemCount() == 0)
            return 0;
        View view = getChildAt(getChildCount() - 1);
        int position = getChildAdapterPosition(view);
        if (position == adapter.getItemCount())
            return Math.max(0, view.getBottom() - getHeight());
        return Integer.MAX_VALUE;
    }

    public static abstract class Pagination extends OnScrollListener {
        private LinearLayoutManager layoutManager;

        public Pagination(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if (layoutManager.getChildCount() + firstVisibleItemPosition >= layoutManager.getItemCount())
                    loadNextPage();
            }
        }

        protected abstract boolean isLoading();

        protected abstract boolean isLastPage();

        protected abstract void loadNextPage();
    }

    Pagination pagination;

    public void setPagination(Pagination pagination) {
        if (this.pagination != null)
            removeOnScrollListener(this.pagination);
        this.pagination = pagination;
        if (pagination != null)
            addOnScrollListener(pagination);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private boolean drawCalled = false;


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


    RevealAnimator revealAnimator;

    public Point getLocationOnScreen() {
        int[] outLocation = new int[2];
        super.getLocationOnScreen(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Point getLocationInWindow() {
        int[] outLocation = new int[2];
        super.getLocationInWindow(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Animator createCircularReveal(android.view.View hotspot, float startRadius, float finishRadius) {
        int[] location = new int[2];
        hotspot.getLocationOnScreen(location);
        int[] myLocation = new int[2];
        getLocationOnScreen(myLocation);
        return createCircularReveal(location[0] - myLocation[0] + hotspot.getWidth() / 2, location[1] - myLocation[1] + hotspot.getHeight() / 2, startRadius, finishRadius);
    }

    @Override
    public Animator createCircularReveal(int x, int y, float startRadius, float finishRadius) {
        startRadius = Carbon.getRevealRadius(this, x, y, startRadius);
        finishRadius = Carbon.getRevealRadius(this, x, y, finishRadius);
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(this, x, y, startRadius, finishRadius);
            circularReveal.setDuration(Carbon.getDefaultRevealDuration());
            return circularReveal;
        } else {
            revealAnimator = new RevealAnimator(x, y, startRadius, finishRadius);
            revealAnimator.setDuration(Carbon.getDefaultRevealDuration());
            revealAnimator.addUpdateListener(animation -> {
                RevealAnimator reveal = ((RevealAnimator) animation);
                reveal.radius = (float) reveal.getAnimatedValue();
                reveal.mask.reset();
                reveal.mask.addCircle(reveal.x, reveal.y, Math.max((Float) reveal.getAnimatedValue(), 1), Path.Direction.CW);
                postInvalidate();
            });
            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    revealAnimator = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    revealAnimator = null;
                }
            });
            return revealAnimator;
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        boolean c = !Carbon.isShapeRect(shapeModel, boundsRect);

        if (Carbon.IS_PIE_OR_HIGHER) {
            if (spotShadowColor != null)
                super.setOutlineSpotShadowColor(spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()));
            if (ambientShadowColor != null)
                super.setOutlineAmbientShadowColor(ambientShadowColor.getColorForState(getDrawableState(), ambientShadowColor.getDefaultColor()));
        }

        // draw not called, we have to handle corners here
        if (isInEditMode() && !drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            dispatchDrawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawPath(cornersMask, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if (!drawCalled && (r || c) && getWidth() > 0 && getHeight() > 0 && !Carbon.IS_LOLLIPOP_OR_HIGHER) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save();
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                dispatchDrawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                dispatchDrawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c) {
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(cornersMask, paint);
            }
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
        } else {
            dispatchDrawInternal(canvas);
        }
        drawCalled = false;
    }

    private void dispatchDrawInternal(@NonNull Canvas canvas) {
        Collections.sort(getViews(), new ElevationComparator());

        super.dispatchDraw(canvas);
        if (stroke != null)
            drawStroke(canvas);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @Override
    public boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        // TODO: why isShown() returns false after being reattached?
        if (child instanceof ShadowView && (!Carbon.IS_LOLLIPOP_OR_HIGHER || ((ShadowView) child).getElevationShadowColor() != null && !Carbon.IS_PIE_OR_HIGHER)) {
            ShadowView shadowView = (ShadowView) child;
            shadowView.drawShadow(canvas);
        }

        if (child instanceof RippleView) {
            RippleView rippleView = (RippleView) child;
            RippleDrawable rippleDrawable = rippleView.getRippleDrawable();
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless) {
                int saveCount = canvas.save();
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
        if (views.size() != childCount)
            getViews();
        return indexOfChild(views.get(child));
    }

    @Override
    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        super.setChildDrawingOrderCallback(childDrawingOrderCallback);
        childDrawingOrderCallbackSet = childDrawingOrderCallback != null;
    }

    protected boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        final Rect frame = new Rect();
        child.getHitRect(frame);
        return frame.contains((int) x, (int) y);
    }


    // -------------------------------
    // corners
    // -------------------------------

    private RectF boundsRect = new RectF();
    private Path cornersMask = new Path();

    public ShapeAppearanceModel getShapeModel() {
        return shapeModel;
    }


    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    @Override
    public void setCornerRadius(float cornerRadius) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new RoundedCornerTreatment(cornerRadius)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setCornerCut(float cornerCut) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new CutCornerTreatment(cornerCut)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setShapeModel(ShapeAppearanceModel model) {
        this.shapeModel = model;
        shadowDrawable = new MaterialShapeDrawable(shapeModel);
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
        if (!Carbon.IS_LOLLIPOP_OR_HIGHER)
            postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        updateCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void updateCorners() {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Carbon.isShapeRect(shapeModel, boundsRect)) {
                        outline.setRect(0, 0, getWidth(), getHeight());
                    } else {
                        shadowDrawable.setBounds(0, 0, getWidth(), getHeight());
                        shadowDrawable.getOutline(outline);
                    }
                }
            });
        }

        boundsRect.set(shadowDrawable.getBounds());
        shadowDrawable.getPathForSize(getWidth(), getHeight(), cornersMask);
    }

    public void drawInternal(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (stroke != null)
            drawStroke(canvas);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(@NonNull Canvas canvas) {
        drawCalled = true;
        boolean r = revealAnimator != null;
        boolean c = !Carbon.isShapeRect(shapeModel, boundsRect);

        if (Carbon.IS_PIE_OR_HIGHER) {
            if (spotShadowColor != null)
                super.setOutlineSpotShadowColor(spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()));
            if (ambientShadowColor != null)
                super.setOutlineAmbientShadowColor(ambientShadowColor.getColorForState(getDrawableState(), ambientShadowColor.getDefaultColor()));
        }

        if (isInEditMode() && (r || c) && getWidth() > 0 && getHeight() > 0) {
            Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas layerCanvas = new Canvas(layer);
            drawInternal(layerCanvas);

            Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(mask);
            Paint maskPaint = new Paint(0xffffffff);
            maskCanvas.drawPath(cornersMask, maskPaint);

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    int maskPixel = mask.getPixel(x, y);
                    layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                }
            }
            canvas.drawBitmap(layer, 0, 0, paint);
        } else if (getWidth() > 0 && getHeight() > 0 && (((r || c) && !Carbon.IS_LOLLIPOP_OR_HIGHER) || !shapeModel.isRoundRect(boundsRect))) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save();
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                drawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                drawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c) {
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(cornersMask, paint);
            }
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            drawInternal(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (shadowDrawable.isPointInTransparentRegion((int) event.getX(), (int) event.getY()))
            return false;
        if (onDispatchTouchListener != null && onDispatchTouchListener.onTouch(this, event))
            return true;

        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            rippleDrawable.setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    @Override
    public void setRippleDrawable(RippleDrawable newRipple) {
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable(rippleDrawable.getBackground());
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            newRipple.setBounds(0, 0, getWidth(), getHeight());
            newRipple.setState(getDrawableState());
            ((Drawable) newRipple).setVisible(getVisibility() == VISIBLE, false);
            if (newRipple.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable((Drawable) newRipple);
        }

        rippleDrawable = newRipple;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateParentIfNeeded();
    }

    private void invalidateParentIfNeeded() {
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
            ((View) getParent()).invalidate();
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    private void postInvalidateParentIfNeededDelayed(long delayMilliseconds) {
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (background instanceof RippleDrawable) {
            setRippleDrawable((RippleDrawable) background);
            return;
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Background) {
            rippleDrawable.setCallback(null);
            rippleDrawable = null;
        }
        super.setBackgroundDrawable(background);
        updateBackgroundTint();
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private ShapeAppearanceModel shapeModel = new ShapeAppearanceModel();
    private MaterialShapeDrawable shadowDrawable = new MaterialShapeDrawable(shapeModel);
    private ColorStateList ambientShadowColor, spotShadowColor;

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setElevation(elevation);
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if (ambientShadowColor == null || spotShadowColor == null) {
                super.setElevation(elevation);
                super.setTranslationZ(translationZ);
            } else {
                super.setElevation(0);
                super.setTranslationZ(0);
            }
        } else if (elevation != this.elevation && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.elevation = elevation;
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if (ambientShadowColor == null || spotShadowColor == null) {
                super.setTranslationZ(translationZ);
            } else {
                super.setTranslationZ(0);
            }
        } else if (translationZ != this.translationZ && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.translationZ = translationZ;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean hasShadow() {
        return getElevation() + getTranslationZ() >= 0.01f && getWidth() > 0 && getHeight() > 0;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        float alpha = getAlpha() * Carbon.getBackgroundTintAlpha(this) / 255.0f;
        if (alpha == 0 || !hasShadow())
            return;

        float z = getElevation() + getTranslationZ();

        int saveCount;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();

        if (alpha != 255) {
            paint.setAlpha((int) (127 * alpha));
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        } else {
            saveCount = canvas.save();
        }
        Matrix matrix = getMatrix();
        canvas.setMatrix(matrix);

        if (r) {
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        shadowDrawable.setFillColor(spotShadowColor);
        shadowDrawable.setShadowColor(spotShadowColor != null ? spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()) : 0xff000000);
        shadowDrawable.setAlpha(0x44);
        shadowDrawable.setElevation(z);
        shadowDrawable.setShadowVerticalOffset(0);
        shadowDrawable.setBounds(getLeft(), (int) (getTop() + z / 4), getRight(), (int) (getBottom() + z / 4));
        shadowDrawable.draw(canvas);

        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        paint.setXfermode(Carbon.CLEAR_MODE);
        if (maskShadow) {
            cornersMask.setFillType(Path.FillType.WINDING);
            canvas.drawPath(cornersMask, paint);
        }
        if (r)
            canvas.drawPath(revealAnimator.mask, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
        paint.setAlpha(255);
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        ambientShadowColor = spotShadowColor = shadowColor;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        ambientShadowColor = spotShadowColor = ColorStateList.valueOf(color);
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return ambientShadowColor;
    }

    @Override
    public void setOutlineAmbientShadowColor(int color) {
        setOutlineAmbientShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineAmbientShadowColor(ColorStateList color) {
        ambientShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineAmbientShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineAmbientShadowColor() {
        return ambientShadowColor.getDefaultColor();
    }

    @Override
    public void setOutlineSpotShadowColor(int color) {
        setOutlineSpotShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineSpotShadowColor(ColorStateList color) {
        spotShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineSpotShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineSpotShadowColor() {
        return spotShadowColor.getDefaultColor();
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin = new Rect();

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin.set(left, top, right, bottom);
    }

    @Override
    public void setTouchMarginLeft(int margin) {
        touchMargin.left = margin;
    }

    @Override
    public void setTouchMarginTop(int margin) {
        touchMargin.top = margin;
    }

    @Override
    public void setTouchMarginRight(int margin) {
        touchMargin.right = margin;
    }

    @Override
    public void setTouchMarginBottom(int margin) {
        touchMargin.bottom = margin;
    }

    @Override
    public Rect getTouchMargin() {
        return touchMargin;
    }

    final RectF tmpHitRect = new RectF();

    public void getHitRect(@NonNull Rect outRect) {
        Matrix matrix = getMatrix();
        if (matrix.isIdentity()) {
            outRect.set(getLeft(), getTop(), getRight(), getBottom());
        } else {
            tmpHitRect.set(0, 0, getWidth(), getHeight());
            matrix.mapRect(tmpHitRect);
            outRect.set((int) tmpHitRect.left + getLeft(), (int) tmpHitRect.top + getTop(),
                    (int) tmpHitRect.right + getLeft(), (int) tmpHitRect.bottom + getTop());
        }
        outRect.left -= touchMargin.left;
        outRect.top -= touchMargin.top;
        outRect.right += touchMargin.right;
        outRect.bottom += touchMargin.bottom;
    }


    // -------------------------------
    // state animators
    // -------------------------------

    private StateAnimator stateAnimator = new StateAnimator(this);

    @Override
    public StateAnimator getStateAnimator() {
        return stateAnimator;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (rippleDrawable != null && rippleDrawable.getStyle() != RippleDrawable.Style.Background)
            rippleDrawable.setState(getDrawableState());
        if (stateAnimator != null)
            stateAnimator.setState(getDrawableState());
        if (tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(getDrawableState());
        if (backgroundTint != null && backgroundTint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) backgroundTint).setState(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private Animator inAnim = null, outAnim = null;
    private Animator animator;

    public Animator animateVisibility(final int visibility) {
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != null) {
                animator = inAnim;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }
                });
                animator.start();
            }
            setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == null) {
                setVisibility(visibility);
                return null;
            }
            animator = outAnim;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        setVisibility(visibility);
                    a.removeListener(this);
                    animator = null;
                }

                @Override
                public void onAnimationCancel(Animator a) {
                    a.removeListener(this);
                    animator = null;
                }
            });
            animator.start();
        } else {
            setVisibility(visibility);
        }
        return animator;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Animator getOutAnimator() {
        return outAnim;
    }

    public void setOutAnimator(Animator outAnim) {
        if (this.outAnim != null)
            this.outAnim.setTarget(null);
        this.outAnim = outAnim;
        if (outAnim != null)
            outAnim.setTarget(this);
    }

    public Animator getInAnimator() {
        return inAnim;
    }

    public void setInAnimator(Animator inAnim) {
        if (this.inAnim != null)
            this.inAnim.setTarget(null);
        this.inAnim = inAnim;
        if (inAnim != null)
            inAnim.setTarget(this);
    }


    // -------------------------------
    // ViewGroup utils
    // -------------------------------

    List<View> views = new ArrayList<>();

    public List<View> getViews() {
        views.clear();
        for (int i = 0; i < getChildCount(); i++)
            views.add(getChildAt(i));
        return views;
    }

    public void setOnDispatchTouchListener(OnTouchListener onDispatchTouchListener) {
        this.onDispatchTouchListener = onDispatchTouchListener;
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
        ViewCompat.postInvalidateOnAnimation(this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(this);
    };

    @Override
    public void setTintList(ColorStateList list) {
        this.tint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    protected void updateTint() {
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
    public void setBackgroundTintList(ColorStateList list) {
        this.backgroundTint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        Drawable background = getBackground();
        if (background instanceof RippleDrawable)
            background = ((RippleDrawable) background).getBackground();
        if (background == null)
            return;

        Carbon.setTintList(background, backgroundTint);
        Carbon.setTintMode(background, backgroundTintMode);
    }

    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
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
        if (this.animateColorChanges == animateColorChanges)
            return;
        this.animateColorChanges = animateColorChanges;
        setTintList(tint);
        setBackgroundTintList(backgroundTint);
    }


    // -------------------------------
    // stroke
    // -------------------------------

    private ColorStateList stroke;
    private float strokeWidth;
    private Paint strokePaint;

    private void drawStroke(Canvas canvas) {
        strokePaint.setStrokeWidth(strokeWidth * 2);
        strokePaint.setColor(stroke.getColorForState(getDrawableState(), stroke.getDefaultColor()));
        cornersMask.setFillType(Path.FillType.WINDING);
        canvas.drawPath(cornersMask, strokePaint);
    }

    @Override
    public void setStroke(ColorStateList colorStateList) {
        stroke = colorStateList;

        if (stroke == null)
            return;

        if (strokePaint == null) {
            strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            strokePaint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void setStroke(int color) {
        setStroke(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getStroke() {
        return stroke;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }


    // -------------------------------
    // maximum width & height
    // -------------------------------

    int maxWidth = Integer.MAX_VALUE, maxHeight = Integer.MAX_VALUE;

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        requestLayout();
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > maxWidth || getMeasuredHeight() > maxHeight) {
            if (getMeasuredWidth() > maxWidth)
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            if (getMeasuredHeight() > maxHeight)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    // -------------------------------
    // transformations
    // -------------------------------

    List<OnTransformationChangedListener> transformationChangedListeners = new ArrayList<>();

    public void addOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.add(listener);
    }

    public void removeOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.remove(listener);
    }

    public void clearOnTransformationChangedListeners() {
        transformationChangedListeners.clear();
    }

    private void fireOnTransformationChangedListener() {
        if (transformationChangedListeners == null)
            return;
        for (OnTransformationChangedListener listener : transformationChangedListeners)
            listener.onTransformationChanged();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            layoutParams.width = width;
            setLayoutParams(layoutParams);
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        } else {
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, height));
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setSize(width, height);
        setTranslationX(x);
        setTranslationY(y);
    }


    // -------------------------------
    // state
    // -------------------------------

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        if (getAdapter() instanceof SelectableItemsAdapter && ((SelectableItemsAdapter) getAdapter()).getSelectionMode() != SelectionMode.NONE)
            ss.selectedIndices = new ArrayList<Integer>(((SelectableItemsAdapter) getAdapter()).getSelectedIndices());

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

        if (ss.selectedIndices != null && ((SelectableItemsAdapter) getAdapter()).getSelectionMode() != SelectionMode.NONE)
            ((SelectableItemsAdapter) getAdapter()).setSelectedIndices(ss.selectedIndices);
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        ArrayList<Integer> selectedIndices;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(RecyclerView.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.selectedIndices = (ArrayList<Integer>) in.readSerializable();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeSerializable(this.selectedIndices);
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

}
