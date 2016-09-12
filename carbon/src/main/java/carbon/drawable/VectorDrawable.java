package carbon.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

/**
 * Created by Marcin on 2015-06-25.
 */
public class VectorDrawable extends Drawable {
    private VectorState state;
    private Bitmap bitmap;
    private ColorStateList tint = ColorStateList.valueOf(0x00ffffff);   // TODO: maybe tint should be a part of VectorState?

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

            float width = svg.getDocumentWidth();
            if (width <= 0)
                width = svg.getDocumentViewBox().width();
            float height = svg.getDocumentHeight();
            if (height <= 0)
                height = svg.getDocumentViewBox().height();

            int intWidth = (int) (width * density);
            int intHeight = (int) (height * density);
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
    public void draw(@NonNull Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();
        if (width <= 0 || height <= 0)
            return;

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

    @NonNull
    @Override
    public Drawable mutate() {
        return new VectorDrawable(state.svg, state.intWidth, state.intHeight);
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

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new VectorDrawable(svg, intWidth, intHeight);
        }

        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }

    @Override
    public void setTint(int tintColor) {
        tint = ColorStateList.valueOf(tintColor);
        state.paint.setColorFilter(new LightingColorFilter(0, tint.getColorForState(getState(), tint.getDefaultColor())));
    }

    @Override
    public void setTintList(ColorStateList tint) {
        this.tint = tint;
        state.paint.setColorFilter(new LightingColorFilter(0, tint.getColorForState(getState(), tint.getDefaultColor())));
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        boolean changed = super.setState(stateSet);
        if (changed)
            state.paint.setColorFilter(new LightingColorFilter(0, tint.getColorForState(getState(), tint.getDefaultColor())));
        return changed;
    }
}
