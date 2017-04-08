package carbon.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import carbon.R;
import carbon.recycler.ArrayAdapter;
import carbon.widget.DropDown;
import carbon.widget.FrameLayout;
import carbon.widget.RecyclerView;

public class DropDownMenu extends PopupWindow {

    protected RecyclerView recycler;
    private View mAnchorView;
    private DropDown.Mode mode;

    public DropDownMenu(Context context) {
        super(View.inflate(context, R.layout.carbon_popupmenu, null));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recycler = (RecyclerView) getContentView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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

    public boolean show(View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        View content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        mAnchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        FrameLayout content = (FrameLayout) getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibilityImmediate(View.VISIBLE);

        return true;
    }

    public void update() {
        if (mAnchorView == null)
            return;

        setClippingEnabled(mode == DropDown.Mode.Fit);

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_padding);
        int itemHeight = (int) res.getDimension(R.dimen.carbon_listItemHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);

        int selectedItem = 0;
        ArrayAdapter adapter = getAdapter();

        if (mAnchorView instanceof android.widget.TextView) {
            TextView textView = (TextView) mAnchorView;
            String text = textView.getText().toString();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).toString().equals(text)) {
                    selectedItem = i;
                    break;
                }
            }
        }

        Rect windowRect = new Rect();
        mAnchorView.getWindowVisibleDisplayFrame(windowRect);
        int hWindow = windowRect.bottom - windowRect.top;
        int wWindow = windowRect.right - windowRect.left;

        int[] location = new int[2];
        mAnchorView.getLocationInWindow(location);

        if (mode == DropDown.Mode.Over) {
            int maxHeightAbove = location[1] - windowRect.top - marginHalf * 2;
            int maxItemsAbove = maxHeightAbove / itemHeight;
            int maxHeightBelow = hWindow - location[1] - marginHalf * 2;
            int maxItemsBelow = maxHeightBelow / itemHeight + 1;

            int itemsBelow = Math.min(adapter.getItemCount() - selectedItem, maxItemsBelow);
            int itemsAbove = Math.min(selectedItem, maxItemsAbove);

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - itemsAbove * itemHeight - (itemHeight - (mAnchorView.getHeight() - mAnchorView.getPaddingTop() - mAnchorView.getPaddingBottom())) / 2 + mAnchorView.getPaddingTop();
            int popupWidth = mAnchorView.getWidth() + margin * 2 + marginHalf * 2 - mAnchorView.getPaddingLeft() - mAnchorView.getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.max(1, itemsAbove + itemsBelow) * itemHeight;

            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            manager.scrollToPositionWithOffset(selectedItem - itemsAbove, 0);

            update(popupX, popupY, popupWidth, popupHeight);
        } else {
            int maxItems = (hWindow - marginHalf * 2 - margin * 2) / itemHeight;

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - (itemHeight - (mAnchorView.getHeight() - mAnchorView.getPaddingTop() - mAnchorView.getPaddingBottom())) / 2 + mAnchorView.getPaddingTop();
            int popupWidth = mAnchorView.getWidth() + margin * 2 + marginHalf * 2 - mAnchorView.getPaddingLeft() - mAnchorView.getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.min(recycler.getAdapter().getItemCount(), maxItems) * itemHeight;

            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            manager.scrollToPosition(selectedItem);

            update(popupX, popupY, popupWidth, popupHeight);
        }

        super.update();
    }

    @Override
    public void dismiss() {
        FrameLayout content = (FrameLayout) getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibility(View.INVISIBLE);
        content.getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                DropDownMenu.super.dismiss();
            }
        });
    }

    public void dismissImmediate() {
        super.dismiss();
    }

    /*
    private int measureContentWidth(android.support.v7.widget.RecyclerView.Adapter adapter) {
        // Menus don't tend to be long, so this is more sane than it looks.
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(getContentView().getContext());
            }
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        return width;
    }*/

    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener listener) {
        getAdapter().setOnItemClickedListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recycler.setAdapter(adapter);
    }

    public ArrayAdapter getAdapter() {
        return (ArrayAdapter) recycler.getAdapter();
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
