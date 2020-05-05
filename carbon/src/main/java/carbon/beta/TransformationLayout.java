package carbon.beta;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import carbon.gesture.GestureDetector;
import carbon.gesture.OnGestureListener;
import carbon.widget.FrameLayout;

public class TransformationLayout extends FrameLayout implements OnGestureListener {

    GestureDetector detector;
    Matrix matrix = new Matrix();
    Matrix dm = new Matrix();
    float minX = 0, maxX = 0, minY = 0, maxY = 0, minScale = 1, maxScale = 2, minRotation = -100, maxRotation = 100;
    float ax = 0, ay = 0, as = 1, ar = 0;
    boolean tx = true, ty = true, sx = true, sy = true, r = true;

    public TransformationLayout(Context context) {
        super(context);
        initTransformationLayout();
    }

    public TransformationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTransformationLayout();
    }

    public TransformationLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTransformationLayout();
    }

    public TransformationLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTransformationLayout();
    }

    private void initTransformationLayout() {
        detector = new GestureDetector(getContext());
        detector.addOnGestureListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (detector.shouldInterceptEvents(event)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onPress(MotionEvent motionEvent) {
    }

    @Override
    public void onTap(MotionEvent motionEvent, int clicks) {
    }

    @Override
    public void onDrag(MotionEvent motionEvent, float translationX, float translationY) {
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public void onTransform(MotionEvent motionEvent, float cx, float cy, float dx, float dy, float dr, float scale) {
        dm.reset();
        dm.postTranslate(-cx, -cy);

        float s2 = Math.max(minScale, Math.min(as * scale, maxScale));
        dm.postScale(sx ? s2 / as : 0, sy ? s2 / as : 0);
        as = s2;
        if (r)
            dm.postRotate((float) ((dr) * 180 / Math.PI));
        dm.postTranslate(cx, cy);
        dm.postTranslate(tx ? dx : 0, ty ? dy : 0);

        TransformedLayout child = (TransformedLayout) getChildAt(0);
        matrix.postConcat(dm);
        child.setMatrix(matrix);
        child.postInvalidate();
    }
}
