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
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.ElevationStateAnimator;
import carbon.animation.StateAnimator;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-11-07.
 */
public class Button extends android.widget.Button implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView {

    public Button(Context context) {
        this(context,null);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Button, defStyleAttr, 0);

        int ap = a.getResourceId(R.styleable.Button_android_textAppearance, -1);
        if (ap != -1) {
            TypedArray appearance = getContext().obtainStyledAttributes(ap, R.styleable.TextAppearance);
            if (appearance != null) {
                for (int i = 0; i < appearance.getIndexCount(); i++) {
                    int attr = appearance.getIndex(i);
                    if (attr == R.styleable.TextAppearance_carbon_textAllCaps) {
                        setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_carbon_textAllCaps, true));
                    } else if (attr == R.styleable.TextAppearance_carbon_textStyle) {
                        setTextStyle(Roboto.Style.values()[appearance.getInt(R.styleable.TextAppearance_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
                    }
                }
            }
            appearance.recycle();
        }

        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.Button_carbon_textAllCaps) {
                setAllCaps(a.getBoolean(R.styleable.Button_carbon_textAllCaps, true));
            } else if (attr == R.styleable.Button_carbon_textStyle) {
                setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.Button_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
            }
        }

        setElevation(a.getDimension(R.styleable.Button_carbon_elevation, 0));

        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        addStateAnimator(new ElevationStateAnimator(this));
        setCornerRadius(a.getDimension(R.styleable.Button_carbon_cornerRadius, 0));

        a.recycle();
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    // -------------------------------
    // corners
    // -------------------------------

    private float cornerRadius;
    private Canvas textureCanvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        Bitmap texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(Canvas canvas) {
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0) {
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

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            ((RippleDrawable) rippleDrawable).setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
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

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (rippleDrawable == null || rippleDrawable.getBackground() == null) {
            super.setBackgroundDrawable(background);
            return;
        }
        rippleDrawable.setBackground(background);
        super.setBackgroundDrawable(rippleDrawable);
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private boolean isRect = true;

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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTranslationZ(enabled ? 0 : -elevation);
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    @Override
    public void setTouchMargin(Rect rect) {
        touchMargin = rect;
    }

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin = new Rect(left, top, right, bottom);
    }

    @Override
    public Rect getTouchMargin() {
        return touchMargin;
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
        if (stateAnimators != null)
            for (StateAnimator animator : stateAnimators)
                animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            AnimUtils.animateIn(this, inAnim, null);
            super.setVisibility(visibility);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    Button.super.setVisibility(visibility);
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


    // -------------------------------
    // roboto
    // -------------------------------

    Roboto.Style style;

    public void setTextStyle(Roboto.Style style) {
        this.style = style;
        if (!isInEditMode())
            super.setTypeface(Roboto.getTypeface(getContext(), style));
    }

    public Roboto.Style getTextStyle() {
        return style;
    }
}
