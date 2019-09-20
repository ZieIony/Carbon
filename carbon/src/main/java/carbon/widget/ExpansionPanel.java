package carbon.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import carbon.R;

public class ExpansionPanel extends LinearLayout {
    ImageView expandedIndicator;
    private boolean expanded = true;
    private FrameLayout content;
    private View header;

    public ExpansionPanel(Context context) {
        super(context);
        initExpansionPanel();
    }

    public ExpansionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initExpansionPanel();
    }

    public ExpansionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initExpansionPanel();
    }

    public ExpansionPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initExpansionPanel();
    }

    private void initExpansionPanel() {
        View.inflate(getContext(), R.layout.carbon_expansionpanel, this);
        expandedIndicator = findViewById(R.id.carbon_groupExpandedIndicator);
        header = findViewById(R.id.carbon_expansionPanelHeader);
        content = findViewById(R.id.carbon_expansionPanelContent);
        setOrientation(VERTICAL);

        setExpanded(true);

        header.setOnClickListener(v -> {
            if (isExpanded()) {
                collapse();
            } else {
                expand();
            }
        });
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    public void expand() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            expandedIndicator.setRotation(180 * (float) (animation.getAnimatedValue()));
            expandedIndicator.postInvalidate();
        });
        animator.start();
        content.setVisibility(VISIBLE);
        expanded = true;
    }

    public void collapse() {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            expandedIndicator.setRotation(180 * (float) (animation.getAnimatedValue()));
            expandedIndicator.postInvalidate();
        });
        animator.start();
        content.setVisibility(GONE);
        expanded = false;
    }

    public void setExpanded(boolean expanded) {
        expandedIndicator.setRotation(expanded ? 180 : 0);
        content.setVisibility(expanded ? VISIBLE : GONE);
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
