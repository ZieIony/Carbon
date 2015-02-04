package pl.zielony.carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Marcin on 2014-12-12.
 */
public class Shadow {
    public Bitmap bitmap;
    public float elevation;

    public Shadow(Bitmap bitmap, float elevation) {
        this.bitmap = bitmap;
        this.elevation = elevation;
    }

    public void draw(Canvas canvas, View view, Paint paint) {
        canvas.drawBitmap(bitmap, -(int) Math.ceil(elevation), -(int) Math.ceil(elevation), paint);
    }


}
