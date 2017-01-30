package carbon.internal;

import android.graphics.Path;

/**
 * Created by Marcin on 2017-01-28.
 */
public class Reveal {
    public float x,y,radius;
    public Path mask;

    public Reveal(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        mask = new Path();
        mask.setFillType(Path.FillType.INVERSE_WINDING);
    }
}
