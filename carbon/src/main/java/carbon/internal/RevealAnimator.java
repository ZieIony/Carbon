package carbon.internal;

import android.animation.ValueAnimator;
import android.graphics.Path;

public class RevealAnimator extends ValueAnimator{
    public float x,y;
    public Path mask;
    public float radius;

    public RevealAnimator(float x, float y, float startRadius, float finishRadius) {
        this.x = x;
        this.y = y;
        mask = new Path();
        mask.setFillType(Path.FillType.INVERSE_WINDING);
        setFloatValues(startRadius,finishRadius);
    }
}
