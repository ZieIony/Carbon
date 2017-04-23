package carbon.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class EditorBackgroundDrawable extends Drawable {
    private final float cornerRadius;
    private final RectF rect;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public EditorBackgroundDrawable(Drawable drawable, float cornerRadius) {
        this.cornerRadius = cornerRadius;
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, 10, 10);
            drawable.draw(canvas);
            int color = bitmap.getPixel(5, 5);
            paint.setColor(color);
        } else {
            paint.setColor(0x00ffffff);
        }
        rect = new RectF();
    }

    public EditorBackgroundDrawable(int color, float cornerRadius) {
        this.cornerRadius = cornerRadius;
        paint.setColor(color);
        rect = new RectF();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        rect.set(getBounds());
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
