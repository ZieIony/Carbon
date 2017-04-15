package carbon.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import carbon.R;

public class DebugOverlay extends PopupWindow {
    private boolean drawBounds = true;
    private boolean drawGrid = true;
    private boolean drawRulers = true;
    private Activity context;

    private ViewTreeObserver.OnPreDrawListener listener = () -> {
        getContentView().postInvalidate();
        return true;
    };

    public DebugOverlay(Activity context) {
        super(context);
        this.context = context;
    }

    public void show() {
        View anchor = context.getWindow().getDecorView().getRootView();
        setContentView(new DebugLayout(context, anchor));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        setTouchable(false);
        setFocusable(false);
        setOutsideTouchable(false);
        setAnimationStyle(0);
        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);
        update(anchor.getWidth(), anchor.getHeight());
        anchor.getViewTreeObserver().addOnPreDrawListener(listener);
    }

    @Override
    public void dismiss() {
        View anchor = context.getWindow().getDecorView().getRootView();
        anchor.getViewTreeObserver().removeOnPreDrawListener(listener);
        super.dismiss();
    }

    public boolean isDrawBoundsEnabled() {
        return drawBounds;
    }

    public void setDrawBoundsEnabled(boolean drawBounds) {
        this.drawBounds = drawBounds;
    }

    public boolean isDrawGridEnabled() {
        return drawGrid;
    }

    public void setDrawGridEnabled(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public boolean isDrawRulersEnabled() {
        return drawRulers;
    }

    public void setDrawRulersEnabled(boolean drawRulers) {
        this.drawRulers = drawRulers;
    }

    private class DebugLayout extends View {
        private final View view;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        float step;
        private int[] location;
        float[] rulers = new float[5];

        public DebugLayout(Context context, View view) {
            super(context);
            this.view = view;

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Resources res = context.getResources();
            step = res.getDimension(R.dimen.carbon_grid8);
            rulers[0] = res.getDimension(R.dimen.carbon_windowPadding);
            rulers[1] = res.getDimension(R.dimen.carbon_windowPadding) + res.getDimension(R.dimen.carbon_iconSize);
            rulers[2] = res.getDimension(R.dimen.carbon_contentSpace) - res.getDimension(R.dimen.carbon_padding);
            rulers[3] = res.getDimension(R.dimen.carbon_contentSpace);
            rulers[4] = wm.getDefaultDisplay().getWidth() - res.getDimension(R.dimen.carbon_windowPadding);
        }

        @Override
        protected void dispatchDraw(@NonNull Canvas canvas) {
            location = new int[2];
            getLocationOnScreen(location);

            paint.setAlpha(255);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);

            if (drawGrid) {
                paint.setColor(0x1f000000);
                for (float x = step; x < getWidth(); x += step) {
                    canvas.drawLine(x, 0, x, getHeight(), paint);
                }
                paint.setColor(0x3f000000);
                for (float y = step; y < getHeight(); y += step) {
                    canvas.drawLine(0, y, getWidth(), y, paint);
                }
            }

            if (drawBounds) {
                List<View> views = findViews();
                for (View v : views)
                    drawView(canvas, v);
            }

            if (drawRulers) {
                paint.setColor(0x7fff00ff);
                for (float ruler : rulers)
                    canvas.drawLine(ruler, 0, ruler, getHeight(), paint);
                paint.setColor(0x3f00ffff);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, 0, rulers[0], getHeight(), paint);
                canvas.drawRect(rulers[2], 0, rulers[3], getHeight(), paint);
                canvas.drawRect(rulers[4], 0, getWidth(), getHeight(), paint);
            }
        }

        private void drawView(Canvas canvas, View v) {
            float vertLine = Math.min(step, v.getWidth() / 3);
            float horzLine = Math.min(step, v.getHeight() / 3);

            int[] l = new int[2];
            v.getLocationOnScreen(l);

            v.getDrawingRect(rect);
            v.getHitRect(rect2);

            rect.offset(l[0] - location[0], l[1] - location[1]);
            rect2.offset(l[0] - location[0] - v.getLeft(), l[1] - location[1] - v.getTop());

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0x3f00ff00);
            canvas.drawRect(rect, paint);

            paint.setColor(0x7f00ff00);
            canvas.drawLine(rect.left, rect.top, rect.left + vertLine, rect.top, paint);
            canvas.drawLine(rect.left, rect.top, rect.left, rect.top + horzLine, paint);

            canvas.drawLine(rect.right, rect.top, rect.right - vertLine, rect.top, paint);
            canvas.drawLine(rect.right, rect.top, rect.right, rect.top + horzLine, paint);

            canvas.drawLine(rect.left, rect.bottom, rect.left + vertLine, rect.bottom, paint);
            canvas.drawLine(rect.left, rect.bottom, rect.left, rect.bottom - horzLine, paint);

            canvas.drawLine(rect.right, rect.bottom, rect.right - vertLine, rect.bottom, paint);
            canvas.drawLine(rect.right, rect.bottom, rect.right, rect.bottom - horzLine, paint);

            if (!rect.equals(rect2)) {
                paint.setColor(0x7fff0000);
                canvas.drawRect(rect2, paint);
            }
        }

        private List<View> findViews() {
            List<View> result = new ArrayList<>();
            List<ViewGroup> groups = new ArrayList<>();
            if (!(view instanceof ViewGroup)) {
                result.add(view);
                return result;
            }
            groups.add((ViewGroup) view);
            while (!groups.isEmpty()) {
                ViewGroup group = groups.remove(0);
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    result.add(child);
                    if (child instanceof ViewGroup)
                        groups.add((ViewGroup) child);
                }
            }
            return result;
        }
    }
}
