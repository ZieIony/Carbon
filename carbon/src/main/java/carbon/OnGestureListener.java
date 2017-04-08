package carbon;

import android.view.MotionEvent;

public interface OnGestureListener {
    void onPress(MotionEvent motionEvent);

    void onTap(MotionEvent motionEvent);

    void onDrag(MotionEvent motionEvent);

    void onMove(MotionEvent motionEvent);

    void onRelease(MotionEvent motionEvent);

    void onLongPress(MotionEvent motionEvent);

    void onMultiTap(MotionEvent motionEvent, int clicks);

    void onCancel(MotionEvent motionEvent);
}
