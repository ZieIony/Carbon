package carbon.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.LinearLayout;
import carbon.widget.ScrollView;

public class Dialog extends DialogBase {

    private ScrollView scrollView;

    public Dialog(@NonNull Context context) {
        super(context);
    }

    public Dialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null), null);
    }

    @Override
    public void setContentView(@NonNull View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        scrollView.addView(view);
        super.setContentView(scrollView, null);
    }

    protected void onContentHeightChanged(int contentHeight) {
        if (scrollView != null && scrollView.getChildCount() > 0 && scrollView.getChildAt(0).getHeight() > contentHeight) {
            if (topDivider != null)
                topDivider.setVisibility(View.VISIBLE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.VISIBLE);
        } else {
            if (topDivider != null)
                topDivider.setVisibility(View.GONE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.GONE);
        }
    }

}
