package carbon.view;

import android.graphics.Path;
import android.graphics.RectF;

import java.security.InvalidParameterException;

import carbon.shadow.ShadowShape;

public class Corners {
    private ShadowShape shape;
    private float topStart, topEnd, bottomStart, bottomEnd;
    private boolean cutTopStart, cutTopEnd, cutBottomStart, cutBottomEnd;
    private RectF rect = new RectF();

    public Corners() {
        this.topStart = 0;
        this.topEnd = 0;
        this.bottomStart = 0;
        this.bottomEnd = 0;
        shape = ShadowShape.CONVEX_PATH;
    }

    public Corners(float corners, boolean cut) {
        this.topStart = corners;
        this.topEnd = corners;
        this.bottomStart = corners;
        this.bottomEnd = corners;
        shape = cut ? ShadowShape.CONVEX_PATH : corners == 0 ? ShadowShape.RECT : ShadowShape.ROUND_RECT;
    }

    public ShadowShape getShape() {
        return shape;
    }

    public float getTopStart() {
        return topStart;
    }

    public void setTopStart(float topStart, boolean cut) {
        if (topStart < 0)
            throw new InvalidParameterException("topStart has to be >= 0");
        this.topStart = topStart;
        cutTopStart = cut;
        shape = ShadowShape.CONVEX_PATH;
    }

    public float getTopEnd() {
        return topEnd;
    }

    public void setTopEnd(float topEnd, boolean cut) {
        if (topEnd < 0)
            throw new InvalidParameterException("topEnd has to be >= 0");
        this.topEnd = topEnd;
        cutTopEnd = cut;
        shape = ShadowShape.CONVEX_PATH;
    }

    public float getBottomStart() {
        return bottomStart;
    }

    public void setBottomStart(float bottomStart, boolean cut) {
        if (bottomStart < 0)
            throw new InvalidParameterException("bottomStart has to be >= 0");
        this.bottomStart = bottomStart;
        cutBottomStart = cut;
        shape = ShadowShape.CONVEX_PATH;
    }

    public float getBottomEnd() {
        return bottomEnd;
    }

    public void setBottomEnd(float bottomEnd, boolean cut) {
        if (bottomEnd < 0)
            throw new InvalidParameterException("bottomEnd has to be >= 0");
        this.bottomEnd = bottomEnd;
        cutBottomEnd = cut;
        shape = ShadowShape.CONVEX_PATH;
    }

    public boolean isZero() {
        return topStart < 1 && topEnd < 1 && bottomStart < 1 && bottomEnd < 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corners corners = (Corners) o;
        return Float.compare(corners.topStart, topStart) == 0 &&
                Float.compare(corners.topEnd, topEnd) == 0 &&
                Float.compare(corners.bottomStart, bottomStart) == 0 &&
                Float.compare(corners.bottomEnd, bottomEnd) == 0;
    }

    public boolean equals(float corner) {
        return Float.compare(corner, topStart) == 0 &&
                Float.compare(corner, topEnd) == 0 &&
                Float.compare(corner, bottomStart) == 0 &&
                Float.compare(corner, bottomEnd) == 0;
    }

    public Path getPath(float width, float height) {
        Path path = new Path();

        float size = Math.min(width, height);
        float ts = Math.min(size, topStart);
        float te = Math.min(size - ts, topEnd);
        float bs = Math.min(size - ts, bottomStart);
        float be = Math.min(Math.min(size - bs, size - te), bottomEnd);

        path.moveTo(0, ts);
        if (cutTopStart) {
            path.lineTo(ts, 0);
        } else {
            rect.set(0, 0, ts * 2, ts * 2);
            path.arcTo(rect, -180, 90, false);
        }
        path.lineTo(width - te, 0);
        if (cutTopEnd) {
            path.lineTo(width, te);
        } else {
            rect.set(width - te * 2, 0, width, te * 2);
            path.arcTo(rect, -90, 90, false);
        }
        path.lineTo(width, height - be);
        if (cutBottomEnd) {
            path.lineTo(width - be, height);
        } else {
            rect.set(width - be * 2, height - be * 2, width, height);
            path.arcTo(rect, 0, 90, false);
        }
        path.lineTo(bs, height);
        if (cutBottomStart) {
            path.lineTo(0, height - bs);
        } else {
            rect.set(0, height - bs * 2, bs * 2, height);
            path.arcTo(rect, 90, 90, false);
        }
        path.close();

        return path;
    }
}
