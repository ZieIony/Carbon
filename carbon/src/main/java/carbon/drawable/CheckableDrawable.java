package carbon.drawable;

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
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.nineoldandroids.animation.ValueAnimator;

import carbon.R;
import carbon.animation.AnimatedColorStateList;

/**
 * Created by Marcin on 2015-03-06.
 */
public class CheckableDrawable extends Drawable {
    private static final long CHECK_DURATION = 100;
    private static final long FILL_DURATION = 100;

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
    private boolean checked, enabled;

    private PorterDuffColorFilter checkedFilter;
    private PorterDuffColorFilter uncheckedFilter;

    private long downTime;
    private BitmapShader checkedShader, filledShader;
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

                filledShader = new BitmapShader(filledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                matrix.postTranslate(bounds.left, bounds.top);
                filledShader.setLocalMatrix(matrix);
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

        long time = System.currentTimeMillis();
        float delta = time - downTime;
        boolean animating = delta < CHECK_DURATION + FILL_DURATION;
        Rect bounds = getBounds();

        if (animating) {
            if (checked && checkedBitmap != null) {
                if (delta < CHECK_DURATION) {
                    paint.setColorFilter(uncheckedFilter);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2, maskBitmap.getHeight() / 2, (1 - delta / FILL_DURATION) * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);
                } else {
                    paint.setShader(null);
                    paint.setColorFilter(uncheckedFilter);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2 + bounds.width() * offset.x, maskBitmap.getHeight() / 2 + bounds.height() * offset.y, (delta - CHECK_DURATION) / FILL_DURATION * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);

                    paint.setShader(checkedShader);
                    paint.setColorFilter(checkedFilter);
                    canvas.drawCircle(bounds.centerX() + bounds.width() * offset.x, bounds.centerY() + bounds.height() * offset.y, (delta - CHECK_DURATION) / FILL_DURATION * radius, paint);
                }
            } else if (!checked && uncheckedBitmap != null) {
                if (delta < CHECK_DURATION) {
                    paint.setShader(null);
                    paint.setColorFilter(uncheckedFilter);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2 + bounds.width() * offset.x, maskBitmap.getHeight() / 2 + bounds.height() * offset.y, (1 - delta / FILL_DURATION) * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);

                    paint.setShader(checkedShader);
                    paint.setColorFilter(checkedFilter);
                    canvas.drawCircle(bounds.centerX() + bounds.width() * offset.x, bounds.centerY() + bounds.height() * offset.y, (1 - delta / FILL_DURATION) * radius, paint);
                } else {
                    paint.setColorFilter(uncheckedFilter);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2, maskBitmap.getHeight() / 2, (delta - CHECK_DURATION) / FILL_DURATION * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);
                }
            }
            invalidateSelf();
        } else {
            if (checked && checkedBitmap != null) {
                paint.setColorFilter(checkedFilter);
                canvas.drawBitmap(checkedBitmap, bounds.left, bounds.top, paint);
            } else if (!checked && uncheckedBitmap != null) {
                paint.setColorFilter(uncheckedFilter);
                canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);
            }
        }
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
            boolean newEnabled = false;
            for (int state : states) {
                if (state == android.R.attr.state_checked)
                    newChecked = true;
                if (state == android.R.attr.state_enabled)
                    newEnabled = true;
            }
            if (checked != newChecked) {
                setChecked(newChecked);
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

    public void setChecked(boolean checked) {
        this.checked = checked;
        downTime = System.currentTimeMillis();
        invalidateSelf();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        invalidateSelf();
    }

    public void setCheckedImmediate(boolean checked) {
        this.checked = checked;
        downTime = System.currentTimeMillis() - CHECK_DURATION - FILL_DURATION;
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

    private boolean animateColorChanges = true;
    private ValueAnimator.AnimatorUpdateListener tintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            updateTint();
            invalidateSelf();
        }
    };

    public boolean isAnimateColorChangesEnabled() {
        return animateColorChanges;
    }

    public void setAnimateColorChangesEnabled(boolean animateColorChanges) {
        this.animateColorChanges = animateColorChanges;
        if (tint != null && !(tint instanceof AnimatedColorStateList))
            setTint(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
    }

    public void setTint(ColorStateList list) {
        this.tint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    public ColorStateList getTint() {
        return tint;
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        this.tintMode = mode;
        updateTint();
    }

    private void updateTint() {
        if (tint == null || tintMode == null) {
            checkedFilter = null;
            uncheckedFilter = null;
        } else {
            checkedFilter = new PorterDuffColorFilter(tint.getColorForState(new int[]{android.R.attr.state_checked, enabled ? android.R.attr.state_enabled : -android.R.attr.state_enabled}, tint.getDefaultColor()), tintMode);
            uncheckedFilter = new PorterDuffColorFilter(tint.getColorForState(new int[]{-android.R.attr.state_checked, enabled ? android.R.attr.state_enabled : -android.R.attr.state_enabled}, tint.getDefaultColor()), tintMode);
        }
        invalidateSelf();
    }
}
