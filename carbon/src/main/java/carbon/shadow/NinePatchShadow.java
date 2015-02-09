package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Marcin on 2014-12-26.
 */
public class NinePatchShadow extends Shadow {


    Rect src = new Rect();
    RectF dst = new RectF();

    public NinePatchShadow(Bitmap bitmap, float elevation) {
        super(bitmap, elevation);
    }

    @Override
    public void draw(Canvas canvas, View view, Paint paint) {
        int e = (int) Math.ceil(elevation);
        int[] xDiv = new int[]{0, e * 2, e * 2 + 1, bitmap.getWidth()};
        int[] yDiv = new int[]{0, e * 2, e * 2 + 1, bitmap.getHeight()};
        int[] xDivDst = new int[]{-e, e, view.getWidth() - e, view.getWidth() + e};
        int[] yDivDst = new int[]{-e, e, view.getHeight() - e, view.getHeight() + e};
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                src.set(xDiv[x], yDiv[y], xDiv[x + 1], yDiv[y + 1]);
                dst.set(xDivDst[x], yDivDst[y], xDivDst[x + 1], yDivDst[y + 1]);
                canvas.drawBitmap(bitmap, src, dst, paint);
            }
        }
    }


}
