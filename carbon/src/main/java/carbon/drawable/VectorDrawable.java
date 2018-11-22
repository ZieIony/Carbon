package carbon.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.util.SparseArray;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class VectorDrawable extends Drawable implements AlphaDrawable, TintAwareDrawable {
    private VectorState state;
    private Bitmap bitmap;

    private static SparseArray<SVG> cache = new SparseArray<>();

    public static void clearCache() {
        cache.clear();
    }

    public VectorDrawable(SVG svg, int intWidth, int intHeight) {
        state = new VectorState(svg, intWidth, intHeight);
        setBounds(0, 0, state.intWidth, state.intHeight);
    }

    public VectorDrawable(Resources res, int resId) {
        if (resId == 0)
            return;
        try {
            SVG svg = cache.get(resId);
            if (svg == null) {
                svg = SVG.getFromResource(res, resId);
                cache.put(resId, svg);
            }
            float density = res.getDisplayMetrics().density;

            float width = svg.getDocumentViewBox().width();
            float height = svg.getDocumentViewBox().height();

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

        updateTint();

        canvas.drawBitmap(bitmap, getBounds().left, getBounds().top, state.paint);
    }

    @Override
    public void setAlpha(int alpha) {
        state.paint.setAlpha(alpha);
    }

    @Override
    public int getAlpha() {
        return state.paint.getAlpha();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        boolean result = super.setState(stateSet);
        updateTint();
        return result;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        state.colorFilter = colorFilter;
        state.tint = null;
        state.tintMode = null;
    }

    @Override
    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        state.colorFilter = null;
        state.tint = tint;
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        state.colorFilter = null;
        state.tintMode = tintMode;
    }

    public void updateTint() {
        if (state.colorFilter != null) {
            state.paint.setColorFilter(state.colorFilter);
        } else if (state.tint != null && state.tintMode != null) {
            state.paint.setColorFilter(new PorterDuffColorFilter(state.tint.getColorForState(getState(), state.tint.getDefaultColor()), state.tintMode));
        } else {
            state.paint.setColorFilter(null);
        }
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
        public ColorFilter colorFilter;
        public ColorStateList tint;
        public PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;

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
            colorFilter = state.colorFilter;
            tint = state.tint;
            tintMode = state.tintMode;
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

}
