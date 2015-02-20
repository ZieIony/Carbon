package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.animation.RippleStateAnimator;
import carbon.animation.StateAnimator;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2015-01-22.
 */
public class ImageView extends android.widget.ImageView implements ShadowView, RippleView {
    private float elevation = 0;
    private float translationZ = 0;
    private boolean isRect = true;

    private Bitmap texture;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Canvas textureCanvas;
    private float cornerRadius;
    private RippleDrawable rippleDrawable;

    public ImageView(Context context) {
        super(context);
        init(null, R.attr.carbon_imageViewStyle);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_imageViewStyle);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageView, defStyleAttr, 0);

        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

        setElevation(a.getDimension(R.styleable.ImageView_carbon_elevation, 0));

        setInAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.ImageView_carbon_inAnimation, 0)]);
        setOutAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.ImageView_carbon_outAnimation, 0)]);
        initTouchMargin(a);
        addStateAnimator(new RippleStateAnimator(this));
        setCornerRadius(a.getDimension(R.styleable.ImageView_carbon_cornerRadius, 0));

        a.recycle();
    }


    // -------------------------------
    // corners
    // -------------------------------

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        initDrawing();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed || getWidth() == 0 || getHeight() == 0)
            return;

        initDrawing();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void initDrawing() {
        if (cornerRadius == 0 || getWidth() == 0 || getHeight() == 0)
            return;
        texture = null;
        texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(Canvas canvas) {
        if (cornerRadius > 0) {
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            super.draw(textureCanvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(textureCanvas);

            RectF rect = new RectF();
            rect.bottom = getHeight();
            rect.right = getWidth();
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
        } else {
            super.draw(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            ((RippleDrawable) rippleDrawable).setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTranslationZ(enabled ? 0 : -elevation);
    }

    @Override
    public Drawable getBackground() {
        return rippleDrawable != null ? rippleDrawable : super.getBackground();
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (rippleDrawable != null && getParent() != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();
    }


    // -------------------------------
    // elevation
    // -------------------------------

    @Override
    public float getElevation() {
        return elevation;
    }

    public synchronized void setElevation(float elevation) {
        elevation = Math.max(0, Math.min(elevation, 25));
        if (elevation == this.elevation)
            return;
        this.elevation = elevation;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public synchronized void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public boolean isRect() {
        return isRect;
    }

    @Override
    public void setRect(boolean rect) {
        this.isRect = rect;
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    private void initTouchMargin(TypedArray a) {
        int touchMarginAll = (int) a.getDimension(R.styleable.ImageView_carbon_touchMargin, 0);
        if (touchMarginAll > 0) {
            touchMargin = new Rect(touchMarginAll, touchMarginAll, touchMarginAll, touchMarginAll);
        } else {
            touchMargin = new Rect();
            int top = (int) a.getDimension(R.styleable.ImageView_carbon_touchMarginTop, 0);
            int left = (int) a.getDimension(R.styleable.ImageView_carbon_touchMarginLeft, 0);
            int right = (int) a.getDimension(R.styleable.ImageView_carbon_touchMarginRight, 0);
            int bottom = (int) a.getDimension(R.styleable.ImageView_carbon_touchMarginBottom, 0);
            if (top > 0 || left > 0 || right > 0 || bottom > 0)
                touchMargin = new Rect(left, top, right, bottom);
        }
    }

    public void getHitRect(Rect outRect) {
        if (touchMargin == null) {
            super.getHitRect(outRect);
            return;
        }
        outRect.set(getLeft() - touchMargin.left, getTop() - touchMargin.top, getRight() + touchMargin.right, getBottom() + touchMargin.bottom);
    }

    // -------------------------------
    // state animators
    // -------------------------------

    private List<StateAnimator> stateAnimators = new ArrayList<>();

    public void removeStateAnimator(StateAnimator animator) {
        stateAnimators.remove(animator);
    }

    public void addStateAnimator(StateAnimator animator) {
        this.stateAnimators.add(animator);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        for (StateAnimator animator : stateAnimators)
            animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    ImageView.super.setVisibility(visibility);
                }
            });
        }
    }

    public AnimUtils.Style getOutAnimation() {
        return outAnim;
    }

    public void setOutAnimation(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnimation() {
        return inAnim;
    }

    public void setInAnimation(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }
}
