package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;

public class Shadow {
    private static final float SHADOW_SCALE = 1.0f;
    public static final int ALPHA = 0x26;

    private final int e;
    final int c;
    private final Bitmap bitmap;
    private Rect src = new Rect();
    private Rect dst = new Rect();
    private int[] xDiv;
    private int[] yDiv;
    private int[] xDivDst;
    private int[] yDivDst;
    public float elevation;

    public static final PorterDuffColorFilter DEFAULT_FILTER = new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);

    public Shadow(Bitmap bitmap, float elevation, int cornerRadius) {
        this.bitmap = bitmap;
        this.elevation = elevation;

        e = (int) (Math.ceil(elevation));
        c = cornerRadius;
        xDiv = new int[]{0, e + c, bitmap.getWidth() - e - c, bitmap.getWidth()};
        yDiv = new int[]{0, e + c, bitmap.getHeight() - e - c, bitmap.getHeight()};
        xDivDst = new int[]{(int) (-e * SHADOW_SCALE), c, 0, 0};
        yDivDst = new int[]{(int) (-e * SHADOW_SCALE), c, 0, 0};
    }

    public void draw(Canvas canvas, View view, Paint paint, ColorFilter colorFilter) {
        xDivDst[2] = view.getWidth() - c;
        yDivDst[2] = view.getHeight() - c;
        xDivDst[3] = (int) (view.getWidth() + e * SHADOW_SCALE);
        yDivDst[3] = (int) (view.getHeight() + e * SHADOW_SCALE);

        paint.setColorFilter(colorFilter);

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
