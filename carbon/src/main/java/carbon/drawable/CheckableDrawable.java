package carbon.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import carbon.R;
import carbon.animation.AnimatedColorStateList;

public class CheckableDrawable extends Drawable implements TintAwareDrawable {

    private float currRadius;
    private int currAnim;
    private ValueAnimator animator;

    public enum CheckedState {
        UNCHECKED, CHECKED, INDETERMINATE
    }

    private static final long CHECK_DURATION = 100;
    private static final long FILL_DURATION = 100;

    private static final int ANIMATION_FILL = 0;
    private static final int ANIMATION_CHECK = 1;

    private final Context context;
    private final int checkedRes;
    private final int uncheckedRes;
    private final int filledRes;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG), maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private PorterDuffXfermode porterDuffSrcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Bitmap checkedBitmap, uncheckedBitmap, filledBitmap, maskBitmap;
    private Canvas maskCanvas;
    private float radius;
    private boolean enabled;
    private CheckedState checkedState = CheckedState.UNCHECKED;

    private BitmapShader checkedShader;
    private PointF offset;
    private ColorStateList tint;
    private PorterDuff.Mode tintMode;

    public CheckableDrawable(Context context, int checkedRes, int uncheckedRes, int filledRes, PointF offset) {
        this.context = context;
        this.checkedRes = checkedRes;
        this.uncheckedRes = uncheckedRes;
        this.filledRes = filledRes;
        this.offset = offset;

        maskPaint.setAlpha(255);
        maskPaint.setColor(0xffffffff);
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        if (!getBounds().equals(bounds))
            checkedBitmap = uncheckedBitmap = filledBitmap = null;
        super.setBounds(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        Rect bounds = getBounds();
        if (bounds.left != left || bounds.right != right || bounds.bottom != bottom || bounds.top != top)
            checkedBitmap = uncheckedBitmap = filledBitmap = null;
        super.setBounds(left, top, right, bottom);
    }

    private void renderSVGs() {
        Rect bounds = getBounds();
        if (bounds.width() <= 0 && bounds.height() <= 0)
            return;

        try {
            {
                SVG svg = SVG.getFromResource(context, checkedRes);
                checkedBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(checkedBitmap);
                svg.setDocumentWidth(checkedBitmap.getWidth());
                svg.setDocumentHeight(checkedBitmap.getHeight());
                svg.renderToCanvas(canvas);

                checkedShader = new BitmapShader(checkedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                matrix.postTranslate(bounds.left, bounds.top);
                checkedShader.setLocalMatrix(matrix);
            }
            {
                SVG svg2 = SVG.getFromResource(context, uncheckedRes);
                uncheckedBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(uncheckedBitmap);
                svg2.setDocumentWidth(uncheckedBitmap.getWidth());
                svg2.setDocumentHeight(uncheckedBitmap.getHeight());
                svg2.renderToCanvas(canvas);
            }
            {
                SVG svg3 = SVG.getFromResource(context, filledRes);
                filledBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(filledBitmap);
                svg3.setDocumentWidth(filledBitmap.getWidth());
                svg3.setDocumentHeight(filledBitmap.getHeight());
                svg3.renderToCanvas(canvas);
            }

            maskBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
            maskCanvas = new Canvas(maskBitmap);
            radius = (float) (Math.sqrt(2) * bounds.width() / 2);
        } catch (SVGParseException e) {
            Log.e(CheckableDrawable.class.getSimpleName(), "There was an error parsing SVG");
        } catch (NullPointerException e) {  // TODO: what is this catch for?
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (checkedBitmap == null)
            renderSVGs();

        Rect bounds = getBounds();

        paint.setColorFilter(new PorterDuffColorFilter(tint.getColorForState(getState(), tint.getDefaultColor()), tintMode));

        if (animator != null && animator.isRunning()) {
            if (currAnim == ANIMATION_FILL) {
                drawUnchecked(canvas, currRadius);
            } else {
                drawChecked(canvas, currRadius);
            }
        } else {
            if (checkedState == CheckedState.CHECKED) {
                canvas.drawBitmap(checkedBitmap, bounds.left, bounds.top, paint);
            } else if (checkedState == CheckedState.UNCHECKED) {
                canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);
            } else {
                canvas.drawBitmap(filledBitmap, bounds.left, bounds.top, paint);
            }
        }
    }

    private ValueAnimator animateFill() {
        if (animator != null && animator.isRunning())
            animator.cancel();
        animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(FILL_DURATION);
        animator.addUpdateListener(animation -> {
            currAnim = ANIMATION_FILL;
            float value = (float) animation.getAnimatedValue();
            currRadius = value * radius;
            invalidateSelf();
        });
        return animator;
    }

    private ValueAnimator animateCheck() {
        if (animator != null && animator.isRunning())
            animator.cancel();
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(CHECK_DURATION);
        animator.addUpdateListener(animation -> {
            currAnim = ANIMATION_CHECK;
            float value = (float) animation.getAnimatedValue();
            currRadius = value * radius;
            invalidateSelf();
        });
        return animator;
    }

    private void drawUnchecked(@NonNull Canvas canvas, float radius) {
        Rect bounds = getBounds();

        canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

        maskCanvas.drawColor(0xffffffff);
        maskPaint.setXfermode(porterDuffClear);
        maskCanvas.drawCircle(maskBitmap.getWidth() / 2, maskBitmap.getHeight() / 2, radius, maskPaint);
        maskPaint.setXfermode(porterDuffSrcIn);
        maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
        canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);
    }

    private void drawChecked(@NonNull Canvas canvas, float radius) {
        Rect bounds = getBounds();

        paint.setShader(null);
        canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

        maskCanvas.drawColor(0xffffffff);
        maskPaint.setXfermode(porterDuffClear);
        maskCanvas.drawCircle(maskBitmap.getWidth() / 2 + bounds.width() * offset.x, maskBitmap.getHeight() / 2 + bounds.height() * offset.y, radius, maskPaint);
        maskPaint.setXfermode(porterDuffSrcIn);
        maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
        canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);

        paint.setShader(checkedShader);
        canvas.drawCircle(bounds.centerX() + bounds.width() * offset.x, bounds.centerY() + bounds.height() * offset.y, radius, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean changed = false;
        if (states != null) {
            boolean newChecked = false;
            boolean newIndeterminate = false;
            boolean newEnabled = false;
            for (int state : states) {
                if (state == android.R.attr.state_checked)
                    newChecked = true;
                if (state == R.attr.carbon_state_indeterminate)
                    newIndeterminate = true;
                if (state == android.R.attr.state_enabled)
                    newEnabled = true;
            }
            CheckedState newCheckedState = newIndeterminate ? CheckedState.INDETERMINATE : newChecked ? CheckedState.CHECKED : CheckedState.UNCHECKED;
            if (checkedState != newCheckedState) {
                setChecked(newCheckedState);
                changed = true;
            }
            if (enabled != newEnabled) {
                setEnabled(newEnabled);
                changed = true;
            }
        }
        boolean result = super.onStateChange(states);
        if (changed && tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(states);
        return result && changed;
    }

    public boolean isChecked() {
        return checkedState == CheckedState.CHECKED;
    }

    public void setChecked(boolean checked) {
        setChecked(checked ? CheckedState.CHECKED : CheckedState.UNCHECKED);
    }

    public void setChecked(CheckedState state) {
        if (checkedState == state)
            return;
        if (checkedState == CheckedState.UNCHECKED) {
            if (state == CheckedState.CHECKED) {
                Animator fill = animateFill();
                fill.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateCheck().start();
                    }
                });
                fill.start();
            } else {
                animateFill().start();
            }
        }
        if (checkedState == CheckedState.CHECKED) {
            if (state == CheckedState.UNCHECKED) {
                ValueAnimator check = animateCheck();
                check.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateFill().reverse();
                    }
                });
                check.reverse();
            } else {
                animateCheck().reverse();
            }
        }
        if (checkedState == CheckedState.INDETERMINATE) {
            if (state == CheckedState.CHECKED) {
                animateCheck().start();
            } else {
                animateFill().reverse();
            }
        }
        checkedState = state;
        invalidateSelf();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        invalidateSelf();
    }

    @Override
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        if (animator != null)
            animator.end();
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.carbon_iconSize);
    }

    @Override
    public int getIntrinsicHeight() {
        return getIntrinsicWidth();
    }

    @NonNull
    @Override
    public Rect getDirtyBounds() {
        return super.getDirtyBounds();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public void setTint(int tintColor) {
        tint = AnimatedColorStateList.fromList(ColorStateList.valueOf(tintColor), __ -> invalidateSelf());
    }

    @Override
    public void setTintList(ColorStateList tint) {
        this.tint = tint instanceof AnimatedColorStateList ? tint : AnimatedColorStateList.fromList(tint, __ -> invalidateSelf());
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        this.tintMode = tintMode;
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        boolean changed = super.setState(stateSet);
        if (changed)
            invalidateSelf();
        return changed;
    }
}
