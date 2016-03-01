package carbon.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import carbon.R;

/**
 * Created by Marcin on 2016-02-29.
 */
public class SeekBarPopup extends PopupWindow {
    private final Context context;
    private final View contentView;
    FrameLayout bubble;
    TextView label;

    public SeekBarPopup(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.carbon_seekbar_bubble, null, false));
        contentView = getContentView();
        label = (TextView) contentView.findViewById(R.id.carbon_label);
        bubble = (FrameLayout) contentView.findViewById(R.id.carbon_bubble);
        this.context = context;

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(false);
        setFocusable(false);
        setOutsideTouchable(false);
        setAnimationStyle(0);
        setClippingEnabled(false);
    }

    @Override
    public void update(int x, int y) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        super.update( 0, y,wm.getDefaultDisplay().getWidth(), contentView.getMeasuredHeight());
        ViewHelper.setTranslationX(bubble, x);
    }

    public boolean show(View anchor) {

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(wm.getDefaultDisplay().getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        super.update(wm.getDefaultDisplay().getWidth(), contentView.getMeasuredHeight());

        bubble.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(wm.getDefaultDisplay().getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        super.update(wm.getDefaultDisplay().getWidth(), contentView.getMeasuredHeight());

        bubble.setVisibilityImmediate(View.VISIBLE);

        return true;
    }

    @Override
    public void dismiss() {
        bubble.setVisibility(View.INVISIBLE);
        Animator animator = bubble.getAnimator();
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SeekBarPopup.super.dismiss();
                }
            });
        }
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public int getBubbleWidth() {
        return bubble.getMeasuredWidth();
    }
}
