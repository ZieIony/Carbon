package carbon.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    private VectorState state;
    Bitmap bitmap;

    public VectorDrawable(SVG svg, int intWidth, int intHeight) {
        state = new VectorState(svg, intWidth, intHeight);
        setBounds(0, 0, state.intWidth, state.intHeight);
    }

    public VectorDrawable(Resources res, int resId) {
        if (resId == 0)
            return;
        try {
            SVG svg = SVG.getFromResource(res, resId);
            float density = res.getDisplayMetrics().density;
            int intWidth = (int) (svg.getDocumentWidth() * density);
            int intHeight = (int) (svg.getDocumentHeight() * density);
            state = new VectorState(svg, intWidth, intHeight);
            setBounds(0, 0, state.intWidth, state.intHeight);
        } catch (SVGParseException e) {

        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        if (state.svg == null || width == 0 || height == 0 || (bitmap != null && bitmap.getWidth() == width && bitmap.getHeight() == height))
            return;
        state.svg.setDocumentWidth(width);
        state.svg.setDocumentHeight(height);
        bitmap = null;
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();
        if(width<=0||height<=0)
            return;

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            state.svg.renderToCanvas(new Canvas(bitmap));
        }
        canvas.drawBitmap(bitmap, getBounds().left, getBounds().top, state.paint);
    }

    @Override
    public void setAlpha(int alpha) {
        state.paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        state.paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return state.intWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return state.intHeight;
    }

    @Override
    public ConstantState getConstantState() {
        return state;
    }

    @Override
    public Drawable mutate() {
        return new VectorDrawable(state.svg, state.intHeight, state.intHeight);
    }

    private class VectorState extends ConstantState {
        SVG svg;
        private Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        int intWidth, intHeight;

        public VectorState(SVG svg, int intWidth, int intHeight) {
            this.svg = svg;
            this.intWidth = intWidth;
            this.intHeight = intHeight;
        }

        public VectorState(VectorState state) {
            svg = state.svg;
            intWidth = state.intWidth;
            intHeight = state.intHeight;
            paint = state.paint;
        }

        @Override
        public Drawable newDrawable() {
            return new VectorDrawable(svg, intWidth, intHeight);
        }

        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }
}
