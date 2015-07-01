package carbon.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

/**
 * Created by Marcin on 2015-06-25.
 */
public class VectorDrawable extends Drawable {
    SVG svg;
    Bitmap bitmap;
    private Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    int density,intWidth,intHeight;

    public VectorDrawable(Resources res, int resId) {
        if (resId == 0)
            return;
        try {
            svg = SVG.getFromResource(res, resId);
            density = res.getDisplayMetrics().densityDpi;
            intWidth = (int)(svg.getDocumentWidth()*res.getDisplayMetrics().density);
            intHeight = (int) (svg.getDocumentHeight()*res.getDisplayMetrics().density);
            setBounds(0, 0, intWidth,intHeight);
        } catch (SVGParseException e) {

        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        if (svg == null || width == 0 || height == 0 || (bitmap != null && bitmap.getWidth() == width && bitmap.getHeight() == height))
            return;
        svg.setDocumentWidth(width);
        svg.setDocumentHeight(height);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setDensity(density);
        svg.renderToCanvas(new Canvas(bitmap));
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null)
            canvas.drawBitmap(bitmap, getBounds().left, getBounds().top, paint);
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
    public int getIntrinsicWidth() {
        return intWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return intHeight;
    }
}
