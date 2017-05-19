package carbon.internal;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class NURBS {
    private int degree;
    private float[] knots;
    private List<PointF> points = new ArrayList<>();

    private float deBoor(int i, int k, float t) {
        if (k == 1) {
            if (knots[i] <= t && t < knots[i + 1]) {
                return 1;
            } else {
                return 0;
            }
        }

        float b1 = deBoor(i, k - 1, t);
        float dt = (knots[i + k - 1] - knots[i]);
        if (dt > 0.00001) {
            b1 /= dt;
        } else {
            b1 = 0;
        }

        float b2 = deBoor(i + 1, k - 1, t);
        dt = (knots[i + k] - knots[i + 1]);
        if (dt > 0.000001) {
            b2 /= dt;
        } else {
            b2 = 0;
        }

        return (t - knots[i]) * b1 + (knots[i + k] - t) * b2;
    }

    public NURBS() {
        degree = 2;
        knots = null;
    }

    public void init() {
        knots = new float[points.size() + degree + 1];
        float div = 0;
        int d = points.size() - (degree + 1);
        float w = 0;
        if (d != 0) {
            div = d + 1.0f;
        }

        for (int i = 0; i < points.size() + degree + 1; i++) {
            if (i > degree && i < points.size() + 1) {
                w++;
            }
            if (d != 0) {
                knots[i] = w / div;
            } else {
                knots[i] = w;
            }
        }
    }

    public void addPoint(PointF p) {
        points.add(p);
    }

    public PointF getPoint(float t) {
        if (degree > points.size())
            throw new IllegalStateException("degree >= points.size()");
        if (t == 0)
            return new PointF(points.get(0).x, points.get(0).y);
        if (t == 1)
            return new PointF(points.get(points.size() - 1).x, points.get(points.size() - 1).y);
        PointF rt = new PointF();
        for (int i = 0; i < points.size(); i++) {
            float ww = deBoor(i, degree + 1, t);
            rt.x += points.get(i).x * ww;
            rt.y += points.get(i).y * ww;
        }
        return rt;
    }
}