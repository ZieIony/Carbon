package carbon.beta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import carbon.Carbon;
import carbon.R;
import carbon.internal.Menu;
import carbon.widget.DropDown;
import carbon.widget.RecyclerView;

public class PopupMenu  extends PopupWindow {

    protected RecyclerView recycler;
    private android.view.View mAnchorView;
    private DropDown.Mode mode;
    private Menu menu;
    private MenuItem.OnMenuItemClickListener listener;

    public PopupMenu(Context context) {
        super(android.view.View.inflate(context, R.layout.carbon_popupmenu, null));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recycler = getContentView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK)) {
                dismiss();
                return true;
            }
            return false;
        });

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
    }

    public boolean show(android.view.View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        carbon.widget.FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.animateVisibility(android.view.View.VISIBLE);

        return true;
    }

    public boolean showImmediate(android.view.View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        carbon.widget.FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibility(android.view.View.VISIBLE);

        return true;
    }

    public void update() {
        if (mAnchorView == null)
            return;

        setClippingEnabled(mode == DropDown.Mode.Fit);

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_margin);
        int itemHeight = (int) res.getDimension(R.dimen.carbon_listItemHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);

        /*int selectedItem = 0;
        ArrayAdapter adapter = getAdapter();

        if (mAnchorView instanceof android.widget.TextView) {
            android.widget.TextView textView = (android.widget.TextView) mAnchorView;
            String text = textView.getText().toString();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).toString().equals(text)) {
                    selectedItem = i;
                    break;
                }
            }
        }*/

        Rect windowRect = new Rect();
        mAnchorView.getWindowVisibleDisplayFrame(windowRect);
        int hWindow = windowRect.bottom - windowRect.top;
        int wWindow = windowRect.right - windowRect.left;

        int[] location = new int[2];
        mAnchorView.getLocationInWindow(location);

        /*if (mode == DropDown.Mode.Over) {
            int maxHeightAbove = location[1] - windowRect.top - marginHalf * 2;
            int maxItemsAbove = maxHeightAbove / itemHeight;
            int maxHeightBelow = hWindow - location[1] - marginHalf * 2;
            int maxItemsBelow = maxHeightBelow / itemHeight + 1;

            int itemsBelow = Math.min(adapter.getItemCount() - selectedItem, maxItemsBelow);
            int itemsAbove = Math.min(selectedItem, maxItemsAbove);

            int popupX = location[0] - margin * 2;
            int popupY = location[1] - margin - marginHalf - itemsAbove * itemHeight - (itemHeight - (mAnchorView.getHeight() - mAnchorView.getPaddingTop() -
                    mAnchorView.getPaddingBottom())) / 2 + mAnchorView.getPaddingTop();
            int popupWidth = mAnchorView.getWidth() + margin * 4 - mAnchorView.getPaddingLeft() - mAnchorView.getPaddingRight();
            int popupHeight = margin * 2 + marginHalf * 2 + Math.max(1, itemsAbove + itemsBelow) * itemHeight;

            popupWidth = Math.min(popupWidth, wWindow - margin * 2);
            popupX = Math.max(popupX, 0);
            popupX = Math.min(popupX, wWindow - popupWidth);

            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            manager.scrollToPositionWithOffset(selectedItem - itemsAbove, 0);

            update(popupX, popupY, popupWidth, popupHeight);
        } else {*/
            int maxItems = (hWindow - marginHalf * 2 - margin * 2) / itemHeight;

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - (itemHeight - (mAnchorView.getHeight() - mAnchorView.getPaddingTop() - mAnchorView.getPaddingBottom())) / 2 + mAnchorView.getPaddingTop();
            int popupWidth = mAnchorView.getWidth() + margin * 2 + marginHalf * 2 - mAnchorView.getPaddingLeft() - mAnchorView.getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.min(recycler.getAdapter().getItemCount(), maxItems) * itemHeight;

            //LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            //manager.scrollToPosition(selectedItem);

            update(popupX, popupY, popupWidth, popupHeight);
       // }

        super.update();
    }

    @Override
    public void dismiss() {
        carbon.widget.FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.animateVisibility(android.view.View.INVISIBLE).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PopupMenu.super.dismiss();
            }
        });
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    public void setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setMenu(int resId) {
        menu = Carbon.getMenu(getContentView().getContext(), resId);
    }

    public void setMenu(final android.view.Menu baseMenu) {
        menu = Carbon.getMenu(getContentView().getContext(), baseMenu);
    }

    public android.view.Menu getMenu() {
        return menu;
    }

    public DropDown.Mode getMode() {
        return mode;
    }

    public void setMode(DropDown.Mode mode) {
        this.mode = mode;
    }

    /* @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (subMenu.hasVisibleItems()) {
            PopupMenu subPopup = new PopupMenu(mContext, subMenu, false);
            subPopup.setCallback(mPresenterCallback);

            boolean preserveIconSpacing = false;
            final int count = subMenu.size();
            for (int i = 0; i < count; i++) {
                MenuItem childItem = subMenu.getItem(i);
                if (childItem.isVisible() && childItem.getIcon() != null) {
                    preserveIconSpacing = true;
                    break;
                }
            }

            if (subPopup.show(mAnchorView)) {
                if (mPresenterCallback != null) {
                    mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }*/

}
