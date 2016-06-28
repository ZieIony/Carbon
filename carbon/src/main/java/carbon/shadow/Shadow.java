package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Marcin on 2014-12-26.
 */
public class Shadow {

    private final int e;
    final int c;
    private final Bitmap bitmap;
    Rect src = new Rect();
    Rect dst = new Rect();
    private int[] xDiv;
    private int[] yDiv;
    private int[] xDivDst;
    private int[] yDivDst;
    public float elevation;

    public Shadow(Bitmap bitmap, float elevation, int cornerRadius) {
        this.bitmap = bitmap;
        this.elevation = elevation;

        e = (int) Math.ceil(elevation);
        c = cornerRadius;
        xDiv = new int[]{0, e + c, bitmap.getWidth() - e - c, bitmap.getWidth()};
        yDiv = new int[]{0, e + c, bitmap.getHeight() - e - c, bitmap.getHeight()};
        xDivDst = new int[]{-e, c, 0, 0};
        yDivDst = new int[]{-e, c, 0, 0};
    }

    public void draw(Canvas canvas, View view, Paint paint) {
        xDivDst[2] = view.getWidth() - c;
        yDivDst[2] = view.getHeight() - c;
        xDivDst[3] = view.getWidth() + e;
        yDivDst[3] = view.getHeight() + e;

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Shadow && elevation == ((Shadow) o).elevation && c == ((Shadow) o).c;
    }
}
