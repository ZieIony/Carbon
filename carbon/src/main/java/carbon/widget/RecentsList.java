package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

import carbon.R;

import static com.nineoldandroids.view.animation.AnimatorProxy.NEEDS_PROXY;
import static com.nineoldandroids.view.animation.AnimatorProxy.wrap;

/**
 * Created by Marcin on 2015-04-13.
 */
@Deprecated
public class RecentsList extends FrameLayout implements GestureDetector.OnGestureListener {
    Scroller scroller;
    RecentsAdapter adapter;
    GestureDetector gestureDetector = new GestureDetector(this);
    int scroll = 0;
    OnItemClickListener onItemClickListener;
    Rect childTouchRect[];

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecentsList(Context context) {
        super(context);
        initRecentsList();
    }

    public RecentsList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRecentsList();
    }

    public RecentsList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecentsList();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecentsList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRecentsList();
    }

    private void initRecentsList() {
        scroller = new Scroller(getContext());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecentsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecentsAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (adapter == null)
            return;

        if (getChildCount() != adapter.getCount())
            initChildren();

        childTouchRect = new Rect[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, 0, getWidth() - getPaddingLeft() - getPaddingRight(), getWidth() - getPaddingLeft() - getPaddingRight());
            childTouchRect[i] = new Rect();
        }
    }

    private void initChildren() {
        removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            final View card = View.inflate(getContext(), R.layout.carbon_recent_card, null);
            TextView title = (TextView) card.findViewById(R.id.carbon_recentTitle);
            title.setText(adapter.getTitle(i));
            android.widget.ImageView icon = (android.widget.ImageView) card.findViewById(R.id.carbon_recentIcon);
            Drawable drawable = adapter.getIcon(i);
            if (drawable == null) {
                icon.setVisibility(View.GONE);
            } else {
                icon.setImageDrawable(drawable);
            }
            View header = card.findViewById(R.id.carbon_recentHeader);
            header.setBackgroundColor(adapter.getHeaderColor(i));
            ViewGroup cardContent = (ViewGroup) card.findViewById(R.id.carbon_recentContent);
            cardContent.addView(adapter.getView(i));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                card.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            addView(card, i, generateDefaultLayoutParams());
            final int finalI = i;
            card.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(card, finalI);
                }
            });
        }
    }

    private void layoutChildren() {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            float topSpace = height - width;
            int y = (int) (topSpace * Math.pow(2, (i * width - scroll) / (float) width));
            float scale = (float) (-Math.pow(2, -y / topSpace / 10.0f) + 19.0f / 10);
            childTouchRect[i].set(getPaddingLeft(), y + getPaddingTop(), (int) (scale * (getPaddingLeft() + getWidth() - getPaddingLeft() - getPaddingRight())), (int) (scale * (y + getPaddingTop() + getWidth() - getPaddingLeft() - getPaddingRight())));
            ViewHelper.setTranslationX(getChildAt(i), getPaddingLeft());
            ViewHelper.setTranslationY(getChildAt(i), y + getPaddingTop());
            ViewHelper.setScaleX(getChildAt(i), scale);
            ViewHelper.setScaleY(getChildAt(i), scale);
        }
    }

    private int getMaxScroll() {
        return (getChildCount() - 1) * (getWidth() - getPaddingLeft() - getPaddingRight());
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        layoutChildren();
        requestLayout();
        super.dispatchDraw(canvas);
        doScrolling();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                MotionEvent e = MotionEvent.obtain(event);
                event.setAction(MotionEvent.ACTION_CANCEL);
                e.offsetLocation(-childTouchRect[i].left, -childTouchRect[i].top);
                getChildAt(i).dispatchTouchEvent(e);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            forceFinished();
        }

        for (int i = getChildCount() - 1; i >= 0; i--) {
            if (childTouchRect[i].contains((int) event.getX(), (int) event.getY())) {
                MotionEvent e = MotionEvent.obtain(event);
                e.offsetLocation(-childTouchRect[i].left, -childTouchRect[i].top);
                if (getChildAt(i).dispatchTouchEvent(e))
                    break;
            }
        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        scroll = (int) Math.max(0, Math.min(scroll + v2, getMaxScroll()));
        postInvalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    void startScrolling(float initialVelocity) {
        scroller.fling(0, scroll, 0, (int) initialVelocity, 0,
                0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        postInvalidate();
    }

    private void doScrolling() {
        if (scroller.isFinished())
            return;

        boolean more = scroller.computeScrollOffset();
        int y = scroller.getCurrY();

        scroll = Math.max(0, Math.min(y, getMaxScroll()));

        if (more)
            postInvalidate();
    }

    boolean isFlinging() {
        return !scroller.isFinished();
    }

    void forceFinished() {
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        startScrolling(-v2);
        return true;
    }


    // -------------------------------
    // transformations
    // -------------------------------

    public float getAlpha() {
        return NEEDS_PROXY ? wrap(this).getAlpha() : super.getAlpha();
    }

    public void setAlpha(float alpha) {
        if (NEEDS_PROXY) {
            wrap(this).setAlpha(alpha);
        } else {
            super.setAlpha(alpha);
        }
    }

    public float getPivotX() {
        return NEEDS_PROXY ? wrap(this).getPivotX() : super.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotX(pivotX);
        } else {
            super.setPivotX(pivotX);
        }
    }

    public float getPivotY() {
        return NEEDS_PROXY ? wrap(this).getPivotY() : super.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotY(pivotY);
        } else {
            super.setPivotY(pivotY);
        }
    }

    public float getRotation() {
        return NEEDS_PROXY ? wrap(this).getRotation() : super.getRotation();
    }

    public void setRotation(float rotation) {
        if (NEEDS_PROXY) {
            wrap(this).setRotation(rotation);
        } else {
            super.setRotation(rotation);
        }
    }

    public float getRotationX() {
        return NEEDS_PROXY ? wrap(this).getRotationX() : super.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationX(rotationX);
        } else {
            super.setRotationX(rotationX);
        }
    }

    public float getRotationY() {
        return NEEDS_PROXY ? wrap(this).getRotationY() : super.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationY(rotationY);
        } else {
            super.setRotationY(rotationY);
        }
    }

    public float getScaleX() {
        return NEEDS_PROXY ? wrap(this).getScaleX() : super.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleX(scaleX);
        } else {
            super.setScaleX(scaleX);
        }
    }

    public float getScaleY() {
        return NEEDS_PROXY ? wrap(this).getScaleY() : super.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleY(scaleY);
        } else {
            super.setScaleY(scaleY);
        }
    }

    public float getTranslationX() {
        return NEEDS_PROXY ? wrap(this).getTranslationX() : super.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationX(translationX);
        } else {
            super.setTranslationX(translationX);
        }
    }

    public float getTranslationY() {
        return NEEDS_PROXY ? wrap(this).getTranslationY() : super.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationY(translationY);
        } else {
            super.setTranslationY(translationY);
        }
    }

    public float getX() {
        return NEEDS_PROXY ? wrap(this).getX() : super.getX();
    }

    public void setX(float x) {
        if (NEEDS_PROXY) {
            wrap(this).setX(x);
        } else {
            super.setX(x);
        }
    }

    public float getY() {
        return NEEDS_PROXY ? wrap(this).getY() : super.getY();
    }

    public void setY(float y) {
        if (NEEDS_PROXY) {
            wrap(this).setY(y);
        } else {
            super.setY(y);
        }
    }
}
