package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2015-02-16.
 */
public class DummyDrawable extends Drawable {
    public Drawable linkedDrawable;

    @Override
    public void draw(Canvas canvas) {
        if(linkedDrawable!=null)
            linkedDrawable.invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
