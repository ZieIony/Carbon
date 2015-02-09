package carbon;

import android.view.MotionEvent;

/**
* Created by Marcin on 2014-12-20.
*/
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
