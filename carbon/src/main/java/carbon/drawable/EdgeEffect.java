package carbon.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class EdgeEffect extends android.widget.EdgeEffect{

    private static final int RECEDE_TIME = 600;
    private static final int PULL_TIME = 167;
    private static final int PULL_DECAY_TIME = 2000;
    private static final float MAX_ALPHA = 0.5f;
    private static final float MAX_GLOW_SCALE = 2.f;
    private static final float PULL_GLOW_BEGIN = 0.f;
    private static final int MIN_VELOCITY = 100;
    private static final int MAX_VELOCITY = 10000;
    private static final float EPSILON = 0.001f;
    private static final double ANGLE = Math.PI / 6;
    private static final float SIN = (float) Math.sin(ANGLE);
    private static final float COS = (float) Math.cos(ANGLE);
    private static final int STATE_IDLE = 0;
    private static final int STATE_PULL = 1;
    private static final int STATE_ABSORB = 2;
    private static final int STATE_RECEDE = 3;
    private static final int STATE_PULL_DECAY = 4;
    private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8f;
    private static final int VELOCITY_GLOW_FACTOR = 6;

    private float mGlowAlpha;
    private float mGlowScaleY;

    private float mGlowAlphaStart;
    private float mGlowAlphaFinish;
    private float mGlowScaleYStart;
    private float mGlowScaleYFinish;

    private long mStartTime;
    private float mDuration;

    private final Interpolator mInterpolator;

    private int mState = STATE_IDLE;

    private float mPullDistance;

    private final Rect mBounds = new Rect();
    private final Paint mPaint = new Paint();
    private float mRadius;
    private float mBaseGlowScale;
    private float mDisplacement = 0.5f;
    private float mTargetDisplacement = 0.5f;

    private RectF rect = new RectF();

    /**
     * Construct a new EdgeEffect with a theme appropriate for the provided context.
     *
     * @param context Context used to provide theming and resource information for the EdgeEffect
     */
    public EdgeEffect(Context context) {
        super(context);

        mPaint.setAntiAlias(true);
        //final TypedArray a = context.obtainStyledAttributes(
        //      com.android.internal.R.styleable.EdgeEffect);
        final int themeColor = Color.RED;//a.getColor(
        //com.android.internal.R.styleable.EdgeEffect_colorEdgeEffect, 0xff666666);
        //a.recycle();
        mPaint.setColor((themeColor & 0xffffff) | 0x33000000);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mInterpolator = new DecelerateInterpolator();
    }

    /**
     * Set the size of this edge effect in pixels.
     *
     * @param width  Effect width in pixels
     * @param height Effect height in pixels
     */
    public void setSize(int width, int height) {
        final float r = width * 0.75f / SIN;
        final float y = COS * r;
        final float h = r - y;
        final float or = height * 0.75f / SIN;
        final float oy = COS * or;
        final float oh = or - oy;

        mRadius = r;
        mBaseGlowScale = h > 0 ? Math.min(oh / h, 1.f) : 1.f;

        mBounds.set(mBounds.left, mBounds.top, width, (int) Math.min(height, h));
    }

    public boolean isFinished() {
        return mState == STATE_IDLE;
    }

    public void finish() {
        mState = STATE_IDLE;
    }

    public void onPull(float deltaDistance) {
        onPull(deltaDistance, 0.5f);
    }

    public void onPull(float deltaDistance, float displacement) {
        final long now = AnimationUtils.currentAnimationTimeMillis();
        mTargetDisplacement = displacement;
        if (mState == STATE_PULL_DECAY && now - mStartTime < mDuration) {
            return;
        }
        if (mState != STATE_PULL) {
            mGlowScaleY = Math.max(PULL_GLOW_BEGIN, mGlowScaleY);
        }
        mState = STATE_PULL;

        mStartTime = now;
        mDuration = PULL_TIME;

        mPullDistance += deltaDistance;

        final float absdd = Math.abs(deltaDistance);
        mGlowAlpha = mGlowAlphaStart = Math.min(MAX_ALPHA,
                mGlowAlpha + (absdd * PULL_DISTANCE_ALPHA_GLOW_FACTOR));

        if (mPullDistance == 0) {
            mGlowScaleY = mGlowScaleYStart = 0;
        } else {
            final float scale = (float) (Math.max(0, 1 - 1 /
                    Math.sqrt(Math.abs(mPullDistance) * mBounds.height()) - 0.3f) / 0.7f);

            mGlowScaleY = mGlowScaleYStart = scale;
        }

        mGlowAlphaFinish = mGlowAlpha;
        mGlowScaleYFinish = mGlowScaleY;
    }

    public void onRelease() {
        mPullDistance = 0;

        if (mState != STATE_PULL && mState != STATE_PULL_DECAY) {
            return;
        }

        mState = STATE_RECEDE;
        mGlowAlphaStart = mGlowAlpha;
        mGlowScaleYStart = mGlowScaleY;

        mGlowAlphaFinish = 0.f;
        mGlowScaleYFinish = 0.f;

        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mDuration = RECEDE_TIME;
    }

    public void onAbsorb(int velocity) {
        mState = STATE_ABSORB;
        velocity = Math.min(Math.max(MIN_VELOCITY, Math.abs(velocity)), MAX_VELOCITY);

        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mDuration = 0.15f + (velocity * 0.02f);

        // The glow depends more on the velocity, and therefore starts out
        // nearly invisible.
        mGlowAlphaStart = 0.3f;
        mGlowScaleYStart = Math.max(mGlowScaleY, 0.f);


        // Growth for the size of the glow should be quadratic to properly
        // respond
        // to a user's scrolling speed. The faster the scrolling speed, the more
        // intense the effect should be for both the size and the saturation.
        mGlowScaleYFinish = Math.min(0.025f + (velocity * (velocity / 100) * 0.00015f) / 2, 1.f);
        // Alpha should change for the glow as well as size.
        mGlowAlphaFinish = Math.max(
                mGlowAlphaStart, Math.min(velocity * VELOCITY_GLOW_FACTOR * .00001f, MAX_ALPHA));
        mTargetDisplacement = 0.5f;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public int getColor() {
        return mPaint.getColor();
    }

    public boolean draw(Canvas canvas) {
        update();

        final int count = canvas.save();

        final float centerX = mBounds.centerX();
        final float centerY = mBounds.height() - mRadius;

        float s = Math.min(mGlowScaleY, 1.f) * mBaseGlowScale;
        canvas.scale(1.f, s, centerX, 0);

        final float displacement = Math.max(0, Math.min(mDisplacement, 1.f)) - 0.5f;
        float translateX = mBounds.width() * displacement / 2;

        canvas.clipRect(mBounds);
        canvas.translate(translateX, 0);
        mPaint.setAlpha((int) (0xff * mGlowAlpha));

        float y = -centerY * s;
        float r = mRadius;
        float r2 = (float) Math.sqrt(-y * y + y * y * s * s + r * r);

        float angle = (float) ((float) Math.acos(y / r2) * 180 / Math.PI);
        rect.set(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);
        canvas.drawArc(rect, 90 - angle, angle * 2, false, mPaint);
        canvas.restoreToCount(count);

        boolean oneLastFrame = false;
        if (mState == STATE_RECEDE && mGlowScaleY == 0) {
            mState = STATE_IDLE;
            oneLastFrame = true;
        }

        return mState != STATE_IDLE || oneLastFrame;
    }

    public int getMaxHeight() {
        return (int) (mBounds.height() * MAX_GLOW_SCALE + 0.5f);
    }

    private void update() {
        final long time = AnimationUtils.currentAnimationTimeMillis();
        final float t = Math.min((time - mStartTime) / mDuration, 1.f);

        final float interp = mInterpolator.getInterpolation(t);

        mGlowAlpha = mGlowAlphaStart + (mGlowAlphaFinish - mGlowAlphaStart) * interp;
        mGlowScaleY = mGlowScaleYStart + (mGlowScaleYFinish - mGlowScaleYStart) * interp;
        mDisplacement = (mDisplacement + mTargetDisplacement) / 2;

        if (t >= 1.f - EPSILON) {
            switch (mState) {
                case STATE_ABSORB:
                    mState = STATE_RECEDE;
                    mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    mDuration = RECEDE_TIME;

                    mGlowAlphaStart = mGlowAlpha;
                    mGlowScaleYStart = mGlowScaleY;

                    // After absorb, the glow should fade to nothing.
                    mGlowAlphaFinish = 0.f;
                    mGlowScaleYFinish = 0.f;
                    break;
                case STATE_PULL:
                    mState = STATE_PULL_DECAY;
                    mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    mDuration = PULL_DECAY_TIME;

                    mGlowAlphaStart = mGlowAlpha;
                    mGlowScaleYStart = mGlowScaleY;

                    // After pull, the glow should fade to nothing.
                    mGlowAlphaFinish = 0.f;
                    mGlowScaleYFinish = 0.f;
                    break;
                case STATE_PULL_DECAY:
                    mState = STATE_RECEDE;
                    break;
                case STATE_RECEDE:
                    mState = STATE_IDLE;
                    break;
            }
        }
    }
}