package carbon.widget;

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

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-12-04.
 */
public class ImageActionButton extends ImageView implements ShadowView, RippleView {
    private float elevation = 0;
    private float translationZ = 0;

    Paint paint = new Paint();
    private boolean isRect = false;

    private AnimUtils.Style inAnim, outAnim;
    private Bitmap texture;
    private Canvas textureCanvas;
    private RippleDrawable rippleDrawable;

    public ImageActionButton(Context context) {
        super(context, null, R.attr.carbon_fabStyle);
        init(null, R.attr.carbon_fabStyle);
    }

    public ImageActionButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_fabStyle);
        init(attrs, R.attr.carbon_fabStyle);
    }

    public ImageActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        paint.setAntiAlias(Carbon.antiAlias);
        paint.setStyle(Paint.Style.FILL);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageActionButton, defStyleAttr, 0);
        Carbon.initRippleDrawable(this, attrs, defStyleAttr);
        setElevation(a.getDimension(R.styleable.ImageActionButton_carbon_elevation, 0));
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.ImageActionButton_carbon_inAnimation, 0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.ImageActionButton_carbon_outAnimation, 0)];
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
                    ImageActionButton.super.setVisibility(visibility);
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
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    @Override
    public void setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
    }
}
