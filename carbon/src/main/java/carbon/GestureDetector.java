package carbon;

import android.os.Handler;
import android.view.MotionEvent;

/**
 * Created by Marcin on 2014-12-20.
 */
public class GestureDetector {

    private static final int DEFAULT_PRESS_TIMEOUT = 100;
    private static final int DEFAULT_TAP_TIMEOUT = 300;
    private static final int DEFAULT_MOVE_EPSILON = 4;
    private int pressTimeout = DEFAULT_PRESS_TIMEOUT;
    private int tapTimeout = DEFAULT_TAP_TIMEOUT;
    private int moveEpsilon = DEFAULT_MOVE_EPSILON;
    private Handler handler;
    Runnable tapHandler;
    Runnable pressHandler;
    private final OnGestureListener listener;
    private long prevTouchTime;
    private float prevTouchY;
    private float prevTouchX;
    boolean verticalMoveCancel = true;
    boolean horizontalMoveCancel = true;
    private boolean dragging;
    private boolean moving;
    private Runnable longPressHandler;
    private long longPressTimeout = 200;
    private int clicks = 0;
    private boolean pressed;

    public GestureDetector(OnGestureListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener cannot be null");
        }
        this.listener = listener;
        handler = new Handler();
    }

    public void onTouchEvent(final MotionEvent motionEvent) {
        final MotionEvent event = MotionEvent.obtain(motionEvent);
        float dx = motionEvent.getX() - prevTouchX;
        float dy = motionEvent.getY() - prevTouchY;
        long time = System.currentTimeMillis();
        long dt = System.currentTimeMillis() - prevTouchTime;
        handler.removeCallbacks(longPressHandler);
        longPressHandler = null;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragging = false;
                moving = false;
                pressed = true;
                if (tapHandler != null) {
                    clicks++;
                    handler.removeCallbacks(tapHandler);
                    tapHandler = null;
                }

                handler.removeCallbacks(pressHandler);
                pressHandler = new Runnable() {

                    @Override
                    public void run() {
                        // handler.removeCallbacks(tapHandler);
                        // tapHandler = null;
                        onPress(event);
                    }

                };
                handler.postDelayed(pressHandler, pressTimeout);

                longPressHandler = new Runnable() {

                    @Override
                    public void run() {
                        onLongPress(event);
                    }
                };
                handler.removeCallbacks(longPressHandler);
                handler.postDelayed(longPressHandler, longPressTimeout);

                prevTouchTime = time;
                break;
            case MotionEvent.ACTION_UP:
                if (pressed || moving || dragging) {
                    if (pressHandler != null) {
                        handler.removeCallbacks(pressHandler);
                        pressHandler.run();
                    }
                    onRelease(event);
                    //break;
                }

                //TODO: tapping is broken
                /*tapHandler = new Runnable() {

                    @Override
                    public void run() {
                        handler.removeCallbacks(longPressHandler);
                        longPressHandler = null;
                        onTap(motionEvent, clicks);
                    }
                };
                handler.postDelayed(tapHandler, tapTimeout - dt);*/

                pressed = false;
                prevTouchTime = time;
                break;

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(dx) > moveEpsilon || Math.abs(dy) > moveEpsilon || dragging || moving) {
                    if (pressHandler == null && !moving) {
                        dragging = true;
                    }
                    handler.removeCallbacks(tapHandler);
                    tapHandler = null;
                    handler.removeCallbacks(pressHandler);
                    pressHandler = null;
                    handler.removeCallbacks(longPressHandler);
                    longPressHandler = null;
                    moving = true;
                    if (dragging) {
                        onDrag(event);
                    } else {
                        onMove(event);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                handler.removeCallbacks(pressHandler);
                pressHandler = null;
                handler.removeCallbacks(tapHandler);
                tapHandler = null;
                handler.removeCallbacks(longPressHandler);
                longPressHandler = null;
                onCancel(event);
        }
        prevTouchX = motionEvent.getX();
        prevTouchY = motionEvent.getY();
    }

    private void onCancel(MotionEvent motionEvent) {
        clicks = 0;
        listener.onCancel(motionEvent);
    }

    private void onRelease(MotionEvent motionEvent) {
        clicks = 0;
        listener.onRelease(motionEvent);
    }

    private void onDrag(MotionEvent motionEvent) {
        clicks = 0;
        listener.onDrag(motionEvent);
    }

    private void onMove(MotionEvent motionEvent) {
        clicks = 0;
        listener.onMove(motionEvent);
    }

    protected void onTap(MotionEvent motionEvent, int clicks) {
        tapHandler = null;
        if (clicks == 0) {
            listener.onTap(motionEvent);
        } else {
            listener.onMultiTap(motionEvent, clicks + 1);
        }
        this.clicks = 0;
    }

    protected void onLongPress(MotionEvent motionEvent) {
        clicks = 0;
        longPressHandler = null;
        listener.onLongPress(motionEvent);
    }

    protected void onPress(MotionEvent motionEvent) {
        pressHandler = null;
        listener.onPress(motionEvent);
    }

    public int getPressTimeout() {
        return pressTimeout;
    }

    public void setPressTimeout(int pressTimeout) {
        this.pressTimeout = pressTimeout;
    }

    public int getTapTimeout() {
        return tapTimeout;
    }

    public void setTapTimeout(int tapTimeout) {
        this.tapTimeout = tapTimeout;
    }

    public int getMoveEpsilon() {
        return moveEpsilon;
    }

    public void setMoveEpsilon(int moveEpsilon) {
        this.moveEpsilon = moveEpsilon;
    }

    public long getLongPressTimeout() {
        return longPressTimeout;
    }

    public void setLongPressTimeout(long longPressTimeout) {
        this.longPressTimeout = longPressTimeout;
    }
}

