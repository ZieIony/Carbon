package carbon.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;

import carbon.Carbon;
import carbon.R;
import carbon.widget.LinearLayout;
import carbon.widget.ScrollView;

public class Dialog extends DialogBase {

    public Dialog(@NonNull Context context) {
        super(context, Carbon.getThemeResId(context, R.attr.dialogTheme));
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
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        scrollView.addView(view);
        super.setContentView(scrollView, null);
    }

}
