package carbon.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import carbon.R;

/**
 * Created by Marcin on 2016-06-08.
 */
public class ExpansionPanel extends RelativeLayout {
    ImageView expandedIndicator;
    private boolean expanded;

    public ExpansionPanel(Context context) {
        super(context);
    }

    public ExpansionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpansionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpansionPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initExpansionPanel() {
        View.inflate(getContext(), R.layout.carbon_expansionpanel, this);
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        expandedIndicator = (ImageView) findViewById(R.id.carbon_groupExpandedIndicator);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapse();
                } else {
                    expand();
                }
            }
        });
    }

    public void expand() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewHelper.setRotation(expandedIndicator, 180 * (float) (animation.getAnimatedValue()));
                expandedIndicator.postInvalidate();
            }
        });
        animator.start();
        expanded = true;
    }

    public void collapse() {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewHelper.setRotation(expandedIndicator, 180 * (float) (animation.getAnimatedValue()));
                expandedIndicator.postInvalidate();
            }
        });
        animator.start();
        expanded = false;
    }

    public void setExpanded(boolean expanded) {
        ViewHelper.setRotation(expandedIndicator, expanded ? 180 : 0);
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
