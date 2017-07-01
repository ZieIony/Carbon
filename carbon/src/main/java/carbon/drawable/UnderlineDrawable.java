package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class UnderlineDrawable extends Drawable {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float thickness = 1;
    private float padding = 0;

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int[] states = getState();
        for (int state : states) {
            if (state == android.R.attr.state_focused) {
                canvas.drawRect(0, bounds.height() - thickness * 2 - padding / 2, bounds.width(), bounds.height() - padding / 2, paint);
                return;
            } else if (state == android.R.attr.state_enabled) {
                canvas.drawRect(0, bounds.height() - thickness - padding / 2, bounds.width(), bounds.height() - padding / 2, paint);
                return;
            }
        }
        //draw disabled dots
        canvas.drawRect(0, bounds.height() - thickness - padding / 2, bounds.width(), bounds.height() - padding / 2, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        return super.setState(stateSet);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public void setPaddingBottom(float padding) {
        this.padding = padding;
    }
}
