package pl.zielony.carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import pl.zielony.carbon.GestureDetector;
import pl.zielony.carbon.Carbon;
import pl.zielony.carbon.OnGestureListener;
import pl.zielony.carbon.R;
import pl.zielony.carbon.animation.AnimUtils;
import pl.zielony.carbon.animation.DefaultAnimatorListener;
import pl.zielony.carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-12-04.
 */
public class SVGActionButton extends SVGView implements ShadowView, OnGestureListener {
    private float elevation = 0;
    private float translationZ = 0;

    Paint paint = new Paint();
    private boolean isRect = false;

    GestureDetector gestureDetector = new GestureDetector(this);
    private AnimUtils.Style inAnim, outAnim;
    private Bitmap texture;
    private Canvas textureCanvas;

    public SVGActionButton(Context context) {
        super(context, null, R.attr.carbon_fabStyle);
        init(null, R.attr.carbon_fabStyle);
    }

    public SVGActionButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_fabStyle);
        init(attrs, R.attr.carbon_fabStyle);
    }

    public SVGActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint.setAntiAlias(Carbon.antiAlias);
        paint.setStyle(Paint.Style.FILL);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SVGActionButton, defStyle, 0);
        int color = a.getColor(R.styleable.SVGActionButton_carbon_rippleColor, 0);
        if (color != 0)
            setBackgroundDrawable(new RippleDrawable(color, getBackground()));
        setElevation(a.getDimension(R.styleable.SVGActionButton_carbon_elevation, 0));
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.SVGActionButton_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.SVGActionButton_carbon_outAnimation, 0)];
        a.recycle();
    }

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    SVGActionButton.super.setVisibility(visibility);
                }
            });
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            texture = null;
            texture = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            textureCanvas = new Canvas(texture);
            paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        super.draw(textureCanvas);

        int radius = (int) Math.ceil(Math.min(getWidth(), getHeight()) / 2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
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
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return false;

        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public float getElevation() {
        return elevation;
    }

    public synchronized void setElevation(float elevation) {
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

    private synchronized void setTranslationZInternal(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void setTranslationZ(float translationZ) {
        ValueAnimator animator = ValueAnimator.ofFloat(this.translationZ, translationZ);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationZInternal((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void onPress(MotionEvent motionEvent) {
        super.onPress(motionEvent);
        setTranslationZ(getResources().getDimension(R.dimen.carbon_translation));
    }

    @Override
    public void onTap(MotionEvent motionEvent) {

    }

    @Override
    public void onDrag(MotionEvent motionEvent) {

    }

    @Override
    public void onMove(MotionEvent motionEvent) {

    }

    @Override
    public void onRelease(MotionEvent motionEvent) {
        super.onRelease(motionEvent);
        setTranslationZ(0);
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public void onMultiTap(MotionEvent motionEvent, int clicks) {

    }

    @Override
    public void onCancel(MotionEvent motionEvent) {
        super.onCancel(motionEvent);
        setTranslationZ(0);
    }
}
