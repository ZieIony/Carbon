package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import carbon.internal.MathUtils;
import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import carbon.widget.TextView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-10.
 */

public class CollapsingLayout extends FrameLayout implements ScrollChild {
    public CollapsingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int onNestedScrollByY(int dy) {
        DependencyLayout.LayoutParams layoutParams = (DependencyLayout.LayoutParams) getLayoutParams();
        int newHeight = MathUtils.constrain(layoutParams.height - dy, getMinimumHeight(), getMaximumHeight());
        setElevation(MathUtils.map(getMaximumHeight(), getMinimumHeight(), 0, getResources().getDimension(carbon.R.dimen.carbon_elevationToolbar), newHeight));
        int usedDy = layoutParams.height - newHeight;
        layoutParams.height = newHeight;
        setLayoutParams(layoutParams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView iconView = (ImageView) toolbar.getIconView();
        TextView titleView = toolbar.getTitleView();
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iconView.getLayoutParams();
            params.gravity = Gravity.TOP;
            iconView.setLayoutParams(params);
        }
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleView.getLayoutParams();
            params.gravity = Gravity.TOP;
            titleView.setLayoutParams(params);
        }

        if (getHeight() == getMinimumHeight() && iconView.getVisibility() != VISIBLE && iconView.getAnimator() == null) {
            iconView.setVisibility(VISIBLE);
        } else if (getHeight() != getMinimumHeight() && iconView.getVisibility() == VISIBLE && iconView.getAnimator() == null) {
            iconView.setVisibility(INVISIBLE);
        }
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, MathUtils.map(getMaximumHeight(), getMinimumHeight(), getResources().getDimension(R.dimen.carbon_textSizeHeadline), getResources().getDimension(carbon.R.dimen.carbon_textSizeTitle), newHeight));
        return usedDy;
    }

    @Override
    public int getNestedScrollRange() {
        return 0;
    }

    @Override
    public int getNestedScrollY() {
        return 0;
    }

    public int getMinimumHeight() {
        return getResources().getDimensionPixelSize(carbon.R.dimen.carbon_toolbarHeight);
    }
}
