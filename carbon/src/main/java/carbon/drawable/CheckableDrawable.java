package carbon.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import carbon.R;

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
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG), maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    PorterDuffXfermode porterDuffClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    PorterDuffXfermode porterDuffSrcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Bitmap checkedBitmap, uncheckedBitmap, filledBitmap, maskBitmap;
    Canvas maskCanvas;
    float radius;
    private boolean checked;

    int checkedColor = 0;
    LightingColorFilter checkedFilter;
    int checkedAlpha;

    int uncheckedColor = 0;
    LightingColorFilter uncheckedFilter;
    int uncheckedAlpha;

    int disabledColor = 0;
    LightingColorFilter disabledFilter;
    int disabledAlpha;

    long downTime;
    private BitmapShader checkedShader, filledShader;
    private PointF offset;
    private ColorStateList color;

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
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        checkedBitmap = uncheckedBitmap = filledBitmap = null;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        checkedBitmap = uncheckedBitmap = filledBitmap = null;
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
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void draw(Canvas canvas) {
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
                    paint.setAlpha(uncheckedAlpha);
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
                    paint.setAlpha(uncheckedAlpha);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2 + bounds.width() * offset.x, maskBitmap.getHeight() / 2 + bounds.height() * offset.y, (delta - CHECK_DURATION) / FILL_DURATION * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);

                    paint.setShader(checkedShader);
                    paint.setColorFilter(checkedFilter);
                    paint.setAlpha(checkedAlpha);
                    canvas.drawCircle(bounds.centerX() + bounds.width() * offset.x, bounds.centerY() + bounds.height() * offset.y, (delta - CHECK_DURATION) / FILL_DURATION * radius, paint);
                }
            } else if (!checked && uncheckedBitmap != null) {
                if (delta < CHECK_DURATION) {
                    paint.setShader(null);
                    paint.setColorFilter(uncheckedFilter);
                    paint.setAlpha(uncheckedAlpha);
                    canvas.drawBitmap(uncheckedBitmap, bounds.left, bounds.top, paint);

                    maskCanvas.drawColor(0xffffffff);
                    maskPaint.setXfermode(porterDuffClear);
                    maskCanvas.drawCircle(maskBitmap.getWidth() / 2 + bounds.width() * offset.x, maskBitmap.getHeight() / 2 + bounds.height() * offset.y, (1 - delta / FILL_DURATION) * radius, maskPaint);
                    maskPaint.setXfermode(porterDuffSrcIn);
                    maskCanvas.drawBitmap(filledBitmap, 0, 0, maskPaint);
                    canvas.drawBitmap(maskBitmap, bounds.left, bounds.top, paint);

                    paint.setShader(checkedShader);
                    paint.setColorFilter(checkedFilter);
                    paint.setAlpha(checkedAlpha);
                    canvas.drawCircle(bounds.centerX() + bounds.width() * offset.x, bounds.centerY() + bounds.height() * offset.y, (1 - delta / FILL_DURATION) * radius, paint);
                } else {
                    paint.setColorFilter(uncheckedFilter);
                    paint.setAlpha(uncheckedAlpha);
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
                paint.setAlpha(checkedAlpha);
                canvas.drawBitmap(checkedBitmap, bounds.left, bounds.top, paint);
            } else if (!checked && uncheckedBitmap != null) {
                paint.setColorFilter(uncheckedFilter);
                paint.setAlpha(uncheckedAlpha);
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
        if (states != null) {
            boolean newChecked = false;
            for (int state : states) {
                if (state == android.R.attr.state_checked)
                    newChecked = true;
            }
            if (checked != newChecked)
                setChecked(newChecked);
        }
        return super.onStateChange(states);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        downTime = System.currentTimeMillis();
        invalidateSelf();
    }

    public void setCheckedImmediate(boolean checked) {
        this.checked = checked;
        downTime = System.currentTimeMillis() - CHECK_DURATION - FILL_DURATION;
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (context.getResources().getDimension(R.dimen.carbon_1dip) * 24);
    }

    @Override
    public int getIntrinsicHeight() {
        return getIntrinsicWidth();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    public int getCheckedColor() {
        return checkedColor;
    }

    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
        checkedFilter = new LightingColorFilter(0, checkedColor);
        checkedAlpha = Color.alpha(checkedColor);
        invalidateSelf();
    }

    public int getUncheckedColor() {
        return uncheckedColor;
    }

    public void setUncheckedColor(int uncheckedColor) {
        this.uncheckedColor = uncheckedColor;
        uncheckedFilter = new LightingColorFilter(0, uncheckedColor);
        uncheckedAlpha = Color.alpha(uncheckedColor);
        invalidateSelf();
    }

    public int getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
        disabledFilter = new LightingColorFilter(0, disabledColor);
        disabledAlpha = Color.alpha(disabledColor);
        invalidateSelf();
    }

    public void setColor(ColorStateList colorStateList) {
        this.color = colorStateList;
        setCheckedColor(color.getColorForState(new int[]{android.R.attr.state_checked}, color.getDefaultColor()));
        setUncheckedColor(color.getColorForState(new int[]{-android.R.attr.state_checked}, color.getDefaultColor()));
        setDisabledColor(color.getColorForState(new int[]{-android.R.attr.state_enabled}, color.getDefaultColor()));
    }

    public ColorStateList getColor() {
        return color;
    }
}
