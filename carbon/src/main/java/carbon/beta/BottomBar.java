package carbon.beta;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.drawable.DefaultColorStateList;
import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import carbon.widget.TextView;

/**
 * Created by Marcin on 17.03.2016.
 */
public class BottomBar extends FrameLayout {
    private LinearLayout content;
    private Menu menu;
    private View activeView;

    public BottomBar(Context context) {
        super(context);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dasd", "dsad");
            }
        });
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(new CarbonContextWrapper(getContext()));
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        content.removeAllViews();
        content.setWeightSum(menu.size());
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            final View view = View.inflate(getContext(), R.layout.carbon_bottombar_item, null);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view == activeView)
                        return;
                    if (activeView != null) {
                        deselectItem(activeView);
                    }
                    selectItem(view);
                }
            });
            ImageView icon = (ImageView) view.findViewById(R.id.carbon_bottomIcon);
            icon.setTint(new DefaultColorStateList(getContext()));
            icon.setImageDrawable(item.getIcon());
            TextView text = (TextView) view.findViewById(R.id.carbon_bottomText);
            text.setTextColor(new DefaultColorStateList(getContext()));
            text.setText(item.getTitle());
            content.addView(view, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
    }

    private void selectItem(final View item) {
        activeView = item;
        item.setSelected(true);
        ImageView icon = (ImageView) item.findViewById(R.id.carbon_bottomIcon);
        final TextView text = (TextView) item.findViewById(R.id.carbon_bottomText);
        ValueAnimator animator = ValueAnimator.ofFloat(getResources().getDimension(R.dimen.carbon_bottomBarInactiveTextSize), getResources().getDimension(R.dimen.carbon_bottomBarActiveTextSize));
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, (Float) animation.getAnimatedValue());
                text.postInvalidate();
            }
        });
        animator.start();
    }

    private void deselectItem(final View item) {
        item.setSelected(false);
        ImageView icon = (ImageView) item.findViewById(R.id.carbon_bottomIcon);
        final TextView text = (TextView) item.findViewById(R.id.carbon_bottomText);
        ValueAnimator animator = ValueAnimator.ofFloat(getResources().getDimension(R.dimen.carbon_bottomBarActiveTextSize), getResources().getDimension(R.dimen.carbon_bottomBarInactiveTextSize));
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, (Float) animation.getAnimatedValue());
                text.postInvalidate();
            }
        });
        animator.start();
    }
}
