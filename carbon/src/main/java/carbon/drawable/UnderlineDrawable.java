package carbon.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.TintAwareDrawable;

public class UnderlineDrawable extends Drawable implements TintAwareDrawable {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float thickness = 1;
    private float padding = 0;
    @Nullable
    private ColorFilter colorFilter;
    @Nullable
    private ColorStateList tint;
    private PorterDuff.Mode tintMode;

    public UnderlineDrawable() {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int[] states = getState();
        boolean focused = false, enabled = false;
        for (int state : states) {
            if (state == android.R.attr.state_focused) {
                focused = true;
            } else if (state == android.R.attr.state_enabled) {
                enabled = true;
            }
        }

        updateTint();

        if (!enabled) {
            for (int i = 0; i < bounds.width(); i += thickness * 3)
                canvas.drawCircle(i + thickness / 2, bounds.height() - thickness / 2 - padding / 2, thickness / 2, paint);
        } else if (focused) {
            canvas.drawRect(0, bounds.height() - thickness * 2 - padding / 2, bounds.width(), bounds.height() - padding / 2, paint);
        } else {
            canvas.drawRect(0, bounds.height() - thickness - padding / 2, bounds.width(), bounds.height() - padding / 2, paint);
        }
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
        this.colorFilter = colorFilter;
        tint = null;
        tintMode = null;
    }

    @Override
    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        colorFilter = null;
        this.tint = tint;
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        colorFilter = null;
        this.tintMode = tintMode;
    }

    public void updateTint() {
        if (colorFilter != null) {
            paint.setColorFilter(colorFilter);
        } else if (tint != null && tintMode != null) {
            paint.setColorFilter(new PorterDuffColorFilter(tint.getColorForState(getState(), tint.getDefaultColor()), tintMode));
        } else {
            paint.setColorFilter(null);
        }
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
