package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by Marcin on 2014-12-26.
 */
public class NinePatchShadow extends Shadow {

    private final int e;
    Rect src = new Rect();
    RectF dst = new RectF();
    private int[] xDiv;
    private int[] yDiv;
    private int[] xDivDst;
    private int[] yDivDst;

    public NinePatchShadow(Bitmap bitmap, float elevation) {
        super(bitmap, elevation);
        e = (int) Math.ceil(elevation);
        xDiv = new int[]{0, e * 2, e * 2 + 1, bitmap.getWidth()};
        yDiv = new int[]{0, e * 2, e * 2 + 1, bitmap.getHeight()};
        xDivDst = new int[]{-e, e, 0, 0};
        yDivDst = new int[]{-e, e, 0, 0};
    }

    @Override
    public void draw(Canvas canvas, View view, Paint paint) {
        xDivDst[2] = view.getWidth()  - e;
        xDivDst[3] = view.getWidth()  + e;
        yDivDst[2] = view.getHeight()  - e;
        yDivDst[3] = view.getHeight()  + e;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (y == 1 && x == 1)
                    continue;
                src.set(xDiv[x], yDiv[y], xDiv[x + 1], yDiv[y + 1]);
                dst.set(xDivDst[x], yDivDst[y], xDivDst[x + 1], yDivDst[y + 1]);
                canvas.drawBitmap(bitmap, src, dst, paint);
            }
        }
    }


}
