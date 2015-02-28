package carbon.internal;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Marcin on 2015-01-25.
 */
public class PopupWindow extends LinearLayout {
    private WindowManager windowManager;
    private boolean visible = false;

    public PopupWindow(Context context) {
        super(context);
        init();
    }

    private void init() {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        setLayoutParams(layoutParams);
        layoutParams.windowAnimations = 0;
    }

    public void show() {
        synchronized (this) {
            if (visible)
                return;
            visible = true;
            windowManager.addView(this, getLayoutParams());
        }
    }

    public void hide() {
        synchronized (this) {
            if (!visible)
                return;
            visible = false;
            try {
                windowManager.removeViewImmediate(this);
            } catch (Exception e) {

            }
        }
    }
}
