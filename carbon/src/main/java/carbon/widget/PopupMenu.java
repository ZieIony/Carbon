package carbon.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import carbon.Carbon;
import carbon.R;
import carbon.internal.MathUtils;
import carbon.recycler.ArrayAdapter;

public class PopupMenu extends PopupWindow {

    protected MenuStrip menuStrip;
    private DropDown.PopupMode popupMode;

    public PopupMenu(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.carbon_popupmenu, (ViewGroup) getContentView(), false);
        menuStrip = view.findViewById(R.id.carbon_menuStrip);
        menuStrip.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK)) {
                dismiss();
                return true;
            }
            return false;
        });

        setContentView(view);
    }

    public void update() {
        if (getAnchorView() == null)
            return;

        setClippingEnabled(popupMode == DropDown.PopupMode.Fit);

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_margin);
        int itemHeight = (int) res.getDimension(R.dimen.carbon_dropdownMenuItemHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);

        int selectedItem = 0;
        ArrayAdapter adapter = menuStrip.getAdapter();

        if (getAnchorView() instanceof android.widget.TextView) {
            android.widget.TextView textView = (TextView) getAnchorView();
            String text = textView.getText().toString();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).toString().equals(text)) {
                    selectedItem = i;
                    break;
                }
            }
        }

        Rect windowRect = new Rect();
        getAnchorView().getWindowVisibleDisplayFrame(windowRect);
        int hWindow = windowRect.bottom - windowRect.top;
        int wWindow = windowRect.right - windowRect.left;

        int[] location = new int[2];
        getAnchorView().getLocationInWindow(location);

        if (popupMode == DropDown.PopupMode.Over) {
            int maxHeightAbove = location[1] - windowRect.top - marginHalf * 2;
            int maxItemsAbove = maxHeightAbove / itemHeight;
            int maxHeightBelow = hWindow - location[1] - marginHalf * 2;
            int maxItemsBelow = Math.max(1, maxHeightBelow / itemHeight);

            int itemsBelow = Math.min(adapter.getItemCount() - selectedItem, maxItemsBelow);
            int itemsAbove = Math.min(selectedItem, maxItemsAbove);

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - itemsAbove * itemHeight - (itemHeight - (getAnchorView().getHeight() - getAnchorView().getPaddingTop() -
                    getAnchorView().getPaddingBottom())) / 2 + getAnchorView().getPaddingTop();
            int popupWidth = getAnchorView().getWidth() + margin * 2 + marginHalf * 2 - getAnchorView().getPaddingLeft() - getAnchorView().getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.max(1, itemsAbove + itemsBelow) * itemHeight;

            popupWidth = Math.min(popupWidth, wWindow - marginHalf * 2);
            if (popupX < 0) {
                popupWidth -= Math.min(-popupX, margin);
                popupX = 0;
            }
            if (popupX + popupWidth > wWindow) {
                int diff = popupX + popupWidth - wWindow;
                diff = Math.min(margin, diff);
                popupWidth -= diff;
                popupX = wWindow - popupWidth;
            }
            popupY = MathUtils.constrain(popupY, 0, hWindow - popupHeight);

            LinearLayoutManager manager = (LinearLayoutManager) menuStrip.getLayoutManager();
            manager.scrollToPositionWithOffset(selectedItem - itemsAbove, 0);

            update(popupX, popupY, popupWidth, popupHeight);
        } else {
            int maxItems = (hWindow - marginHalf * 2 - margin * 2) / itemHeight;

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - (itemHeight - (getAnchorView().getHeight() - getAnchorView().getPaddingTop() - getAnchorView().getPaddingBottom())) / 2 + getAnchorView().getPaddingTop();
            int popupWidth = getAnchorView().getWidth() + margin * 2 + marginHalf * 2 - getAnchorView().getPaddingLeft() - getAnchorView().getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.min(menuStrip.getAdapter().getItemCount(), maxItems) * itemHeight;

            LinearLayoutManager manager = (LinearLayoutManager) menuStrip.getLayoutManager();
            manager.scrollToPosition(selectedItem);

            update(popupX, popupY, popupWidth, popupHeight);
        }

        menuStrip.layout(0, 0, getWidth(), getHeight());
        menuStrip.setAdapter(menuStrip.getAdapter());
        super.update();
    }

    public void setOnMenuItemClickListener(RecyclerView.OnItemClickedListener<MenuStrip.Item> listener) {
        menuStrip.setOnItemClickedListener(listener);
    }

    public void setMenu(int resId) {
        menuStrip.setMenu(Carbon.getMenu(getContentView().getContext(), resId));
    }

    public void setMenu(final android.view.Menu baseMenu) {
        menuStrip.setMenu(baseMenu);
    }

    public DropDown.PopupMode getPopupMode() {
        return popupMode;
    }

    public void setPopupMode(DropDown.PopupMode popupMode) {
        this.popupMode = popupMode;
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

            if (subPopup.show(anchorView)) {
                if (mPresenterCallback != null) {
                    mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }*/

}
