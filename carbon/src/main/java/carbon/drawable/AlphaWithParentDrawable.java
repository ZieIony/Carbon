package carbon.drawable;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlphaWithParentDrawable extends Drawable {

    public interface Marker {
    }

    private View owner;
    private Drawable alphaDrawable;

    public AlphaWithParentDrawable(View owner, Drawable alphaDrawable) {
        this.owner = owner;
        this.alphaDrawable = alphaDrawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        try {
            Drawable bg = null;
            View p = owner;
            while (true) {
                p = (View) p.getParent();
                if (p == null)
                    break;
                bg = p.getBackground();
                if (bg != null)
                    break;
            }
            if (bg == null)
                bg = ((Activity) owner.getContext()).getWindow().getDecorView().getBackground();
            bg.draw(canvas);
        } catch (Exception e) {
        }
        alphaDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int i) {
        alphaDrawable.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        alphaDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return alphaDrawable.getOpacity();
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        alphaDrawable.setBounds(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        alphaDrawable.setBounds(left, top, right, bottom);
    }
}
