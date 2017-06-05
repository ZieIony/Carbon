package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import carbon.GestureDetector;
import carbon.OnGestureAdapter;
import carbon.widget.RelativeLayout;

public class BehaviorLayout extends RelativeLayout {
    private Map<View, Behavior> behaviors = new HashMap<>();
    GestureDetector detector = new GestureDetector(new OnGestureAdapter(){
        @Override
        public void onDrag(MotionEvent motionEvent) {
            super.onDrag(motionEvent);
        }
    });

    public BehaviorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public void addBehavior(View view, Behavior behavior) {
        behaviors.put(view, behavior);
    }
}
