package carbon.gesture;

import android.view.MotionEvent;

public interface OnGestureListener {
    void onPress(MotionEvent motionEvent);

    void onTap(MotionEvent motionEvent, int clicks);

    void onDrag(MotionEvent motionEvent, float translationX, float translationY);

    void onLongPress(MotionEvent motionEvent);

    void onTransform(MotionEvent motionEvent, float cx, float cy, float dx, float dy, float rx, float scale);
}
