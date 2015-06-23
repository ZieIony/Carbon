package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.R;
import carbon.drawable.ControlFocusedColorStateList;

/**
 * Created by Marcin on 2015-06-10.
 */
public class PopupMenu extends PopupWindow implements View.OnKeyListener, TintedView {

    private RecyclerView recycler;
    private View mAnchorView;

    public PopupMenu(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.carbon_popup, null, false));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recycler = (RecyclerView) getContentView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setOnKeyListener(this);

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
    }

    public boolean show(View anchor) {
        mAnchorView = anchor;

        super.showAsDropDown(anchor);

        update();

        View content = getContentView().findViewById(R.id.carbon_popupContent);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        mAnchorView = anchor;

        super.showAsDropDown(anchor);

        update();

        FrameLayout content = (FrameLayout) getContentView().findViewById(R.id.carbon_popupContent);
        content.setVisibilityImmediate(View.VISIBLE);

        return true;
    }

    public void update() {
        if (mAnchorView == null)
            return;

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_padding);
        int height = (int) res.getDimension(R.dimen.carbon_toolbarHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);

        int selectedItem = 0;

        if (mAnchorView instanceof android.widget.TextView) {
            TextView textView = (TextView) mAnchorView;
            String text = textView.getText().toString();
            RecyclerView.Adapter adapter = getAdapter();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).equals(text)) {
                    selectedItem = i;
                    break;
                }
            }
        }

        int popupX = -margin;
        int popupY = -margin - mAnchorView.getHeight() - marginHalf;

        int maxHeight = getMaxAvailableHeight(mAnchorView, popupY);

        int popupWidth = mAnchorView.getWidth() + margin * 2;
        int items = maxHeight / height;
        int popupHeight = marginHalf * 2 + items * height + margin * 2;

        LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
        manager.scrollToPositionWithOffset(selectedItem, 0);

        update(mAnchorView, popupX, popupY, popupWidth, popupHeight);

        super.update();
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK)) {
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void dismiss() {
        FrameLayout content = (FrameLayout) getContentView().findViewById(R.id.carbon_popupContent);
        content.setVisibility(View.INVISIBLE);
        content.getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PopupMenu.super.dismiss();
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

    public void setAdapter(RecyclerView.Adapter adapter) {
        recycler.setAdapter(adapter);
        adapter.setOnItemClickedListener(new RecyclerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                dismiss();
            }
        });
    }

    public RecyclerView.Adapter getAdapter() {
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


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;

    @Override
    public void setTint(ColorStateList list) {
        this.tint = list != null ? list : new ControlFocusedColorStateList(getContentView().getContext());
        recycler.setTint(list);
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }
}
