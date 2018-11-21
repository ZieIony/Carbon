package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.View;

import carbon.view.Corners;

public class Shadow {
    public static final int ALPHA = 47;

    private final int e;
    private final Bitmap bitmap;
    private Rect src = new Rect();
    private Rect dst = new Rect();
    public float elevation;
    public Corners corners;

    public static final PorterDuffColorFilter DEFAULT_FILTER = new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);

    public Shadow(Bitmap bitmap, float elevation, Corners corners) {
        this.bitmap = bitmap;
        this.elevation = elevation;

        e = (int) (Math.ceil(elevation));
        this.corners = corners;
    }

    public void draw(Canvas canvas, View view, Paint paint, ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);

        float ts = corners.getTopStart();
        float te = corners.getTopEnd();
        float bs = corners.getBottomStart();
        float be = corners.getBottomEnd();
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = view.getWidth();
        int vh = view.getHeight();

        // top
        src.set(0,
                0,
                (int) (e + ts),
                (int) (e + ts));
        dst.set(-e,
                -e,
                (int) (ts),
                (int) (ts));
        canvas.drawBitmap(bitmap, src, dst, paint);

        if (ts < vw - te) {
            src.set((int) (e + ts),
                    0,
                    (int) (bw - e - te),
                    2 * e);
            dst.set((int) (ts),
                    -e,
                    (int) (vw - te),
                    e);
            canvas.drawBitmap(bitmap, src, dst, paint);
        }

        src.set((int) (bw - e - te),
                0,
                bw,
                (int) (e + te));
        dst.set((int) (vw - te),
                -e,
                vw + e,
                (int) (te));
        canvas.drawBitmap(bitmap, src, dst, paint);

        canvas.clipRect((int) (ts),
                -e,
                (int) (vw - te),
                e, Region.Op.DIFFERENCE);

        // center
        if (ts < vh - bs) {
            src.set(0,
                    (int) (e + ts),
                    2 * e,
                    (int) (bh - e - bs));
            dst.set(-e,
                    (int) (ts),
                    e,
                    (int) (vh - bs));
            canvas.drawBitmap(bitmap, src, dst, paint);
        }

        if (te < vh - be) {
            src.set(bw - 2 * e,
                    (int) (e + ts),
                    bw,
                    (int) (bh - e - bs));
            dst.set(vw - e,
                    (int) (te),
                    vw + e,
                    (int) (vh - be));
            canvas.drawBitmap(bitmap, src, dst, paint);
        }

        //bottom
        src.set(0,
                (int) (bh - bs - e),
                (int) (e + bs),
                bh);
        dst.set(-e,
                (int) (vh - bs),
                (int) (bs),
                vh + e);
        canvas.drawBitmap(bitmap, src, dst, paint);

        canvas.clipRect(-e,
                (int) (ts),
                e,
                (int) (vh - bs), Region.Op.DIFFERENCE);

        canvas.clipRect(vw - e,
                (int) (te),
                vw + e,
                (int) (vh - be), Region.Op.DIFFERENCE);

        if (bs < vw - be) {
            src.set((int) (e + bs),
                    bh - 2 * e,
                    (int) (bw - e - be),
                    bh);
            dst.set((int) (bs),
                    vh - e,
                    (int) (vw - be),
                    vh + e);
            canvas.drawBitmap(bitmap, src, dst, paint);
        }

        src.set((int) (bw - e - be),
                (int) (bh - be - e),
                bw,
                bh);
        dst.set((int) (vw - be),
                (int) (vh - be),
                vw + e,
                vh + e);
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Shadow && elevation == ((Shadow) o).elevation && corners.equals(((Shadow) o).corners);
    }
}
