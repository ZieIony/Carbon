package carbon.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2015-02-20.
 */
public class CornersDrawable extends Drawable {
    private Drawable drawable;
    float cornerRadius = 0;
    private Bitmap texture;
    private Canvas textureCanvas;
    private Paint paint;


    public CornersDrawable(Drawable drawable) {
        this.drawable = drawable;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        texture = null;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        texture = null;
    }

    private void initDrawing() {
        texture = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
        textureCanvas = new Canvas(texture);
        paint.setShader(new BitmapShader(texture, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(Canvas canvas) {
        if (cornerRadius > 0) {
            if (texture == null)
                initDrawing();
            textureCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            if (drawable != null)
                drawable.draw(textureCanvas);

            RectF rect = new RectF();
            rect.right = getBounds().width();
            rect.bottom = getBounds().height();
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
        } else {
            if (drawable != null)
                drawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (drawable != null)
            drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (drawable != null)
            drawable.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        if (drawable != null)
            return drawable.getOpacity();
        return 0;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        texture = null;
        invalidateSelf();
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
