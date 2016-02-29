package carbon.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.R;

/**
 * Created by Marcin on 2016-02-29.
 */
public class SeekBarPopup extends PopupWindow {
    TextView label;

    public SeekBarPopup(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.carbon_seekbar_label, null, false));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        label = (TextView) getContentView().findViewById(R.id.carbon_label);

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(false);
        setFocusable(false);
        setOutsideTouchable(false);
        setAnimationStyle(0);
        setClippingEnabled(false);
    }

    public boolean show(View anchor) {

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        View content = getContentView().findViewById(R.id.carbon_popupContent);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        LinearLayout content = (LinearLayout) getContentView().findViewById(R.id.carbon_menuContainer);
        content.setVisibilityImmediate(View.VISIBLE);

        return true;
    }

    @Override
    public void dismiss() {
        FrameLayout content = (FrameLayout) getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibility(View.INVISIBLE);
        content.getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                SeekBarPopup.super.dismiss();
            }
        });
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    public void setText(String text) {
        label.setText(text);
    }
}
