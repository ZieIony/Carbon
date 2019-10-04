package carbon.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import carbon.R;

public class DebugOverlay extends PopupWindow {
    private boolean drawBounds = true;
    private boolean drawMargins = true;
    private boolean drawPaddings = true;
    private boolean drawGrid = false;
    private boolean drawRulers = false;
    private boolean drawHitRects = true;
    private boolean drawTextSizes = false;
    private Activity context;
    private static SparseIntArray marginColors = new SparseIntArray();

    private static List<Integer> colors = new ArrayList<>();
    private ViewTreeObserver.OnPreDrawListener listener = () -> {
        getContentView().postInvalidate();
        return true;
    };

    public DebugOverlay(Activity context) {
        super(context);
        this.context = context;

        if (colors.isEmpty()) {
            colors.add(context.getResources().getColor(R.color.carbon_red_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_pink_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_purple_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_deepPurple_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_indigo_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_blue_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_lightBlue_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_cyan_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_teal_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_green_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_lightGreen_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_lime_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_yellow_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_amber_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_orange_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_deepOrange_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_brown_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_grey_400) & 0xffffff | 0x7f000000);
            colors.add(context.getResources().getColor(R.color.carbon_blueGrey_400) & 0xffffff | 0x7f000000);
        }
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

    public boolean isDrawMarginsEnabled() {
        return drawMargins;
    }

    public void setDrawMarginsEnabled(boolean drawMargins) {
        this.drawMargins = drawMargins;
    }

    public boolean isDrawPaddingsEnabled() {
        return drawPaddings;
    }

    public void setDrawPaddingsEnabled(boolean drawPaddings) {
        this.drawPaddings = drawPaddings;
    }

    public boolean isDrawHitRectsEnabled() {
        return drawHitRects;
    }

    public void setDrawHitRectsEnabled(boolean drawHitRects) {
        this.drawHitRects = drawHitRects;
    }

    public boolean isDrawTextSizesEnabled() {
        return drawTextSizes;
    }

    public void setDrawTextSizesEnabled(boolean drawTextSizes) {
        this.drawTextSizes = drawTextSizes;
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

            if (view instanceof ViewGroup) {
                drawViewGroup(canvas, (ViewGroup) view);
            } else {
                drawView(canvas, view);
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
            int[] l = new int[2];
            v.getLocationOnScreen(l);

            rect.set(0, 0, v.getWidth(), v.getHeight());
            rect.offset(l[0] - location[0], l[1] - location[1]);

            if (drawMargins)
                drawMargins(canvas, v);

            if (drawPaddings)
                drawPaddings(canvas, v);

            if (drawBounds)
                drawBounds(canvas, v);

            if (drawHitRects) {
                v.getHitRect(rect2);
                rect2.offset(l[0] - location[0] - v.getLeft(), l[1] - location[1] - v.getTop());

                if (!rect.equals(rect2)) {
                    paint.setColor(0x7fff0000);
                    canvas.drawRect(rect2, paint);
                }
            }

            if (drawTextSizes && v instanceof TextView) {
                TextView tv = (TextView) v;
                paint.setTextSize(12);
                float textSize = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
                paint.setColor(Color.WHITE);
                paint.setShadowLayer(2, 0, 0, 0xff000000);
                canvas.drawText(textSize + "sp", rect.left, rect.top + paint.getTextSize(), paint);
            }
        }

        private void drawBounds(Canvas canvas, View v) {
            paint.setStyle(Paint.Style.STROKE);

            float vertLine = Math.min(step, v.getWidth() / 3);
            float horzLine = Math.min(step, v.getHeight() / 3);

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
        }

        private void drawPaddings(Canvas canvas, View v) {
            paint.setStyle(Paint.Style.FILL);

            if (v.getPaddingTop() > 0) {
                paint.setColor(getColor(v.getPaddingTop()));
                canvas.drawRect(rect.left, rect.top + v.getPaddingTop(), rect.right, rect.top, paint);
            }

            if (v.getPaddingBottom() > 0) {
                paint.setColor(getColor(v.getPaddingBottom()));
                canvas.drawRect(rect.left, rect.bottom, rect.right, rect.bottom - v.getPaddingBottom(), paint);
            }

            if (v.getPaddingLeft() > 0) {
                paint.setColor(getColor(v.getPaddingLeft()));
                canvas.drawRect(rect.left + v.getPaddingLeft(), rect.top, rect.left, rect.bottom, paint);
            }

            if (v.getPaddingRight() > 0) {
                paint.setColor(getColor(v.getPaddingRight()));
                canvas.drawRect(rect.right, rect.top, rect.right - v.getPaddingRight(), rect.bottom, paint);
            }
        }

        private void drawMargins(Canvas canvas, View v) {
            paint.setStyle(Paint.Style.FILL);

            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLp = (ViewGroup.MarginLayoutParams) layoutParams;

                if (marginLp.topMargin > 0) {
                    paint.setColor(getColor(marginLp.topMargin));
                    canvas.drawRect(
                            rect.left - marginLp.leftMargin,
                            rect.top - marginLp.topMargin,
                            rect.right + marginLp.rightMargin,
                            rect.top,
                            paint);
                }

                if (marginLp.bottomMargin > 0) {
                    paint.setColor(getColor(marginLp.bottomMargin));
                    canvas.drawRect(
                            rect.left - marginLp.leftMargin,
                            rect.bottom,
                            rect.right + marginLp.rightMargin,
                            rect.bottom + marginLp.bottomMargin,
                            paint);
                }

                if (marginLp.leftMargin > 0) {
                    paint.setColor(getColor(marginLp.leftMargin));
                    canvas.drawRect(
                            rect.left - marginLp.leftMargin,
                            rect.top - marginLp.topMargin,
                            rect.left,
                            rect.bottom + marginLp.bottomMargin,
                            paint);
                }

                if (marginLp.rightMargin > 0) {
                    paint.setColor(getColor(marginLp.rightMargin));
                    canvas.drawRect(
                            rect.right,
                            rect.top - marginLp.topMargin,
                            rect.right + marginLp.rightMargin,
                            rect.bottom + marginLp.bottomMargin,
                            paint);
                }
            }
        }

        void drawViewGroup(Canvas canvas, ViewGroup viewGroup) {
            drawView(canvas, viewGroup);
            canvas.save();

            int[] l = new int[2];
            viewGroup.getLocationOnScreen(l);
            rect.set(0, 0, viewGroup.getWidth(), viewGroup.getHeight());
            rect.offset(l[0] - location[0], l[1] - location[1]);

            canvas.clipRect(rect);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    drawViewGroup(canvas, (ViewGroup) v);
                } else {
                    drawView(canvas, v);
                }
            }
            canvas.restore();
        }
    }

    private int getColor(int index) {
        int color = marginColors.get(index);
        if (color == 0)
            color = colors.get(marginColors.size() % colors.size());
        marginColors.put(index, color);
        return color;
    }
}
