package carbon.gesture;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

import carbon.R;

public class GestureDetector {

    private static final int DEFAULT_PRESS_TIMEOUT = 100;
    private static final int DEFAULT_LONGPRESS_TIMEOUT = 200;
    private static final int DEFAULT_TAP_TIMEOUT = 300;

    private int pressTimeout = DEFAULT_PRESS_TIMEOUT;
    private long longPressTimeout = DEFAULT_LONGPRESS_TIMEOUT;
    private int tapTimeout = DEFAULT_TAP_TIMEOUT;
    private int moveEpsilon;

    private Handler handler = new Handler();
    private Runnable pressHandler;
    private Runnable longPressHandler;
    private Runnable tapHandler;

    private float prevTouchY, startTouchX;
    private float prevTouchX, startTouchY;
    private float prevCenterX, prevCenterY, prevDist, prevRotation;

    private boolean pressed;
    private int clicks = 0;
    private boolean moving, transforming = false;

    public GestureDetector(Context context){
        moveEpsilon = context.getResources().getDimensionPixelSize(R.dimen.carbon_moveEpsilon);
    }

    private List<OnGestureListener> listeners = new ArrayList<>();

    public void addOnGestureListener(OnGestureListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener cannot be null");
        listeners.add(listener);
    }

    public void removeOnGestureListener(OnGestureListener listener){
        if (listener == null)
            throw new NullPointerException("Listener cannot be null");
        listeners.remove(listener);
    }

    public void clearOnGestureListeners(){
        listeners.clear();
    }

    public boolean shouldInterceptEvents(MotionEvent event){
        if(event.getPointerCount()>1)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moving = false;
                pressed = true;
                startTouchX=event.getX();
                startTouchY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(pressed) {
                    if (!moving) {
                        float dx = event.getX() - startTouchX;
                        float dy = event.getY() - startTouchY;
                        if (Math.sqrt(dx * dx + dy * dy) > moveEpsilon)
                            return true;
                    } else {
                        return true;
                    }
                }
            break;
        }
        return false;
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final MotionEvent event = MotionEvent.obtain(motionEvent);
        handler.removeCallbacks(longPressHandler);
        longPressHandler = null;

        if (motionEvent.getPointerCount() == 1) {
            transforming = false;
            handleSinglePointer(event);
        } else if (motionEvent.getPointerCount() == 2) {
            handleTwoPointers(event);
        } else {
        }

        prevTouchX = motionEvent.getX();
        prevTouchY = motionEvent.getY();

        return moving;
    }

    private void handleSinglePointer(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                moving = false;
                pressed = true;
                startTouchX=event.getX();
                startTouchY=event.getY();

                clicks++;
                handler.removeCallbacks(tapHandler);
                tapHandler = null;

                pressHandler = () -> firePressEvent(event);
                handler.postDelayed(pressHandler, pressTimeout);

                longPressHandler = () -> fireLongPressEvent(event);
                handler.removeCallbacks(longPressHandler);
                handler.postDelayed(longPressHandler, longPressTimeout);

                break;

            case MotionEvent.ACTION_UP:
                if (pressed&&pressHandler != null) {
                    handler.removeCallbacks(pressHandler);
                    pressHandler.run();
                }

                if (clicks > 0) {
                    fireTapEvent(event, clicks);

                    tapHandler = () -> {
                        tapHandler = null;
                        clicks=0;
                    };
                    handler.postDelayed(tapHandler, tapTimeout);
                }

                pressed = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if(pressed) {
                    if (!moving) {
                        float dx = event.getX() - startTouchX;
                        float dy = event.getY() - startTouchY;
                        if (Math.sqrt(dx * dx + dy * dy) > moveEpsilon) {
                            handler.removeCallbacks(tapHandler);
                            tapHandler = null;
                            handler.removeCallbacks(pressHandler);
                            pressHandler = null;
                            handler.removeCallbacks(longPressHandler);
                            longPressHandler = null;
                            moving = true;
                            fireDragEvent(event, dx, dy);
                        }
                    } else {
                        fireDragEvent(event, event.getX() - prevTouchX, event.getY() - prevTouchY);
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                moving = false;
                handler.removeCallbacks(pressHandler);
                pressHandler = null;
                handler.removeCallbacks(tapHandler);
                tapHandler = null;
                handler.removeCallbacks(longPressHandler);
                longPressHandler = null;
                clicks=0;
        }
    }

    private void handleTwoPointers(MotionEvent event) {
        clicks=0;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(!transforming) {
                    transforming = true;
                    handler.removeCallbacks(pressHandler);
                    pressHandler = null;
                    handler.removeCallbacks(longPressHandler);
                    longPressHandler = null;
                    handler.removeCallbacks(tapHandler);
                    tapHandler = null;

                    prevRotation = (float) Math.atan2(event.getY(0) - event.getY(1), event.getX(0) - event.getX(1));
                    prevDist = (float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1), 2) +
                            Math.pow(event.getY(0) - event.getY(1), 2));
                    prevCenterX = (event.getX(0) + event.getX(1)) / 2;
                    prevCenterY = (event.getY(0) + event.getY(1)) / 2;
                }else {
                    float dist = (float) Math.sqrt(Math.pow(event.getX(0) - event.getX(1), 2) +
                            Math.pow(event.getY(0) - event.getY(1), 2));
                    float cx = (event.getX(0) + event.getX(1)) / 2;
                    float cy = (event.getY(0) + event.getY(1)) / 2;
                    float dx = cx-prevCenterX;
                    float dy = cy-prevCenterY;

                    float rotation = (float) Math.atan2(event.getY(0) - event.getY(1), event.getX(0) - event.getX(1));
                    float scale = dist / prevDist;
                    float rx = rotation - prevRotation;

                    fireTransformEvent(event, cx,cy,dx,dy,rx,scale);

                    prevCenterX = cx;
                    prevCenterY = cy;
                    prevRotation = rotation;
                    prevDist = dist;
                }
                break;
        }
    }

    private void fireTransformEvent(MotionEvent motionEvent, float cx, float cy, float dx, float dy, float rx, float scale) {
        for(OnGestureListener listener:listeners)
            listener.onTransform(motionEvent, cx,cy,dx,dy,rx,scale);
    }

    private void fireDragEvent(MotionEvent motionEvent, float translationX, float translationY) {
        clicks = 0;
        for(OnGestureListener listener:listeners)
            listener.onDrag(motionEvent, translationX, translationY);
    }

    private void fireTapEvent(MotionEvent motionEvent, int clicks) {
        tapHandler = null;
        for(OnGestureListener listener:listeners)
            listener.onTap(motionEvent, clicks);
    }

    private void fireLongPressEvent(MotionEvent motionEvent) {
        clicks = 0;
        longPressHandler = null;
        for(OnGestureListener listener:listeners)
            listener.onLongPress(motionEvent);
    }

    private void firePressEvent(MotionEvent motionEvent) {
        pressHandler = null;
        for(OnGestureListener listener:listeners)
            listener.onPress(motionEvent);
    }

    public int getPressTimeout() {
        return pressTimeout;
    }

    public void setPressTimeout(int pressTimeout) {
        this.pressTimeout = pressTimeout;
    }

    public long getLongPressTimeout() {
        return longPressTimeout;
    }

    public void setLongPressTimeout(long longPressTimeout) {
        this.longPressTimeout = longPressTimeout;
    }

    public int getTapTimeout() {
        return tapTimeout;
    }

    /**
     * @param tapTimeout time between subsequent release events to be interpreted as another tap
     */
    public void setTapTimeout(int tapTimeout) {
        this.tapTimeout = tapTimeout;
    }

    public int getMoveEpsilon() {
        return moveEpsilon;
    }

    /**
     * @param moveEpsilon distance over which move and drag events are reported
     */
    public void setMoveEpsilon(int moveEpsilon) {
        this.moveEpsilon = moveEpsilon;
    }
}

