package carbon.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import carbon.R;

/**
 * Created by Marcin on 2016-02-16.
 */
public class FloatingActionMenu extends PopupWindow {
    private Menu menu;
    private View mAnchorView;

    public FloatingActionMenu(Context context) {
        super(new LinearLayout(context));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
        setClippingEnabled(false);
    }

    public boolean show(View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        return true;
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(getContentView().getContext());
        MenuInflater inflater = new MenuInflater(getContentView().getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;

        LinearLayout content = (LinearLayout) getContentView();
        content.setOrientation(android.widget.LinearLayout.VERTICAL);

        for (int i = 0; i < menu.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(content.getContext());
            View view = inflater.inflate(R.layout.carbon_floatingactionmenu_right, content, false);
            TextView tv = (TextView) view.findViewById(R.id.carbon_tooltip);
            FloatingActionButton iv = (FloatingActionButton) view.findViewById(R.id.carbon_fab);
            tv.setText(menu.getItem(i).getTitle());
            iv.setImageDrawable(menu.getItem(i).getIcon());
            content.addView(view);
        }
    }

    public Menu getMenu() {
        return menu;
    }
}
