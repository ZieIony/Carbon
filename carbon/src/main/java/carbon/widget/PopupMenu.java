package carbon.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import carbon.R;

/**
 * Created by Marcin on 2015-06-10.
 */
public class PopupMenu extends PopupWindow implements View.OnKeyListener,
        ViewTreeObserver.OnGlobalLayoutListener, PopupWindow.OnDismissListener {

    private RecyclerView recycler;
    private View mAnchorView;
    private ViewTreeObserver mTreeObserver;

    public PopupMenu(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.popup_menu_item_layout, null, false));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recycler = (RecyclerView) getContentView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public boolean show(View anchor) {
        mAnchorView = anchor;
        setOnDismissListener(this);

        //setModal(true);
        setFocusable(true);

        if (anchor != null) {
            final boolean addGlobalListener = mTreeObserver == null;
            mTreeObserver = anchor.getViewTreeObserver(); // Refresh to latest
            if (addGlobalListener) mTreeObserver.addOnGlobalLayoutListener(this);
        } else {
            return false;
        }

        final Resources res = getContentView().getContext().getResources();
        int maxWidth = res.getDisplayMetrics().widthPixels / 2;

        setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        super.showAsDropDown(anchor);

        int margin = (int) res.getDimension(R.dimen.carbon_padding);
        int[] location = new int[2];
        mAnchorView.getLocationInWindow(location);
        update(location[0] - margin, location[1] - margin, Math.min(mAnchorView.getWidth() * margin, maxWidth), 400);
        return true;
    }

    public void onDismiss() {
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) mTreeObserver = mAnchorView.getViewTreeObserver();
            mTreeObserver.removeGlobalOnLayoutListener(this);
            mTreeObserver = null;
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
            dismiss();
            return true;
        }
        return false;
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

    @Override
    public void onGlobalLayout() {
        if (isShowing()) {
            final View anchor = mAnchorView;
            if (anchor == null || !anchor.isShown()) {
                dismiss();
            } else if (isShowing()) {
                // Recompute window size and position
                show(mAnchorView);
            }
        }
    }

    public void onViewDetachedFromWindow(View v) {
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) mTreeObserver = v.getViewTreeObserver();
            mTreeObserver.removeGlobalOnLayoutListener(this);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recycler.setAdapter(adapter);
        adapter.setOnItemClickedListener(new RecyclerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                dismiss();
            }
        });
    }

    public RecyclerView.Adapter getAdapter(){
        return (RecyclerView.Adapter) recycler.getAdapter();
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
