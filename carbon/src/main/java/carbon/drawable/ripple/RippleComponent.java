package carbon.drawable.ripple;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Abstract class that handles hardware/software hand-off and lifecycle for animated ripple
 * foreground and background components.
 */
abstract class RippleComponent {
    private final RippleDrawableICS mOwner;

    /**
     * Bounds used for computing max radius. May be modified by the owner.
     */
    protected final Rect mBounds;

    private Animator mSoftwareAnimator;

    /**
     * Whether we have an explicit maximum radius.
     */
    private boolean mHasMaxRadius;

    /**
     * How big this ripple should be when fully entered.
     */
    protected float mTargetRadius;

    /**
     * Screen density used to adjust pixel-based constants.
     */
    protected float mDensity;

    public RippleComponent(RippleDrawableICS owner, Rect bounds) {
        mOwner = owner;
        mBounds = bounds;
    }

    public void onBoundsChange() {
        if (!mHasMaxRadius) {
            mTargetRadius = getTargetRadius(mBounds);
            onTargetRadiusChanged(mTargetRadius);
        }
    }

    public final void setup(float maxRadius, float density) {
        if (maxRadius >= 0) {
            mHasMaxRadius = true;
            mTargetRadius = maxRadius;
        } else {
            mTargetRadius = getTargetRadius(mBounds);
        }

        mDensity = density;

        onTargetRadiusChanged(mTargetRadius);
    }

    private static float getTargetRadius(Rect bounds) {
        final float halfWidth = bounds.width() / 2.0f;
        final float halfHeight = bounds.height() / 2.0f;
        return (float) Math.sqrt(halfWidth * halfWidth + halfHeight * halfHeight);
    }

    /**
     * Starts a ripple enter animation.
     *
     * @param fast whether the ripple should enter quickly
     */
    public final void enter(boolean fast) {
        cancel();

        mSoftwareAnimator = createSoftwareEnter(fast);

        if (mSoftwareAnimator != null) {
            mSoftwareAnimator.start();
        }
    }

    /**
     * Starts a ripple exit animation.
     */
    public final void exit() {
        cancel();

        mSoftwareAnimator = createSoftwareExit();
        mSoftwareAnimator.start();
    }

    /**
     * Cancels all animations. Software animation values are left in the current state, while
     * hardware animation values jump to the end state.
     */
    public void cancel() {
        cancelSoftwareAnimations();
    }

    /**
     * Ends all animations, jumping values to the end state.
     */
    public void end() {
        endSoftwareAnimations();
    }

    /**
     * Draws the ripple to the canvas, inheriting the paint's color and alpha properties.
     *
     * @param c the canvas to which the ripple should be drawn
     * @param p the paint used to draw the ripple
     * @return {@code true} if something was drawn, {@code false} otherwise
     */
    public boolean draw(Canvas c, Paint p) {
        return drawSoftware(c, p);
    }

    /**
     * Populates {@code bounds} with the maximum drawing bounds of the ripple relative to its
     * center. The resulting bounds should be translated into parent drawable coordinates before
     * use.
     *
     * @param bounds the rect to populate with drawing bounds
     */
    public void getBounds(Rect bounds) {
        final int r = (int) Math.ceil(mTargetRadius);
        bounds.set(-r, -r, r, r);
    }

    /**
     * Cancels any current software animations, leaving the values in their current state.
     */
    private void cancelSoftwareAnimations() {
        if (mSoftwareAnimator != null) {
            mSoftwareAnimator.cancel();
            mSoftwareAnimator = null;
        }
    }

    /**
     * Ends any current software animations, jumping the values to their end state.
     */
    private void endSoftwareAnimations() {
        if (mSoftwareAnimator != null) {
            mSoftwareAnimator.end();
            mSoftwareAnimator = null;
        }
    }

    protected final void invalidateSelf() {
        mOwner.invalidateSelf(false);
    }

    protected final void onHotspotBoundsChanged() {
        if (!mHasMaxRadius) {
            final float halfWidth = mBounds.width() / 2.0f;
            final float halfHeight = mBounds.height() / 2.0f;
            final float targetRadius = (float) Math.sqrt(halfWidth * halfWidth
                    + halfHeight * halfHeight);

            onTargetRadiusChanged(targetRadius);
        }
    }

    /**
     * Called when the target radius changes.
     *
     * @param targetRadius the new target radius
     */
    protected void onTargetRadiusChanged(float targetRadius) {
        // Stub.
    }

    protected abstract Animator createSoftwareEnter(boolean fast);

    protected abstract Animator createSoftwareExit();

    protected abstract boolean drawSoftware(Canvas c, Paint p);

    /**
     * Called when the hardware exit is cancelled. Jumps software values to end state to ensure that
     * software and hardware values are synchronized.
     */
    protected abstract void jumpValuesToExit();

}
