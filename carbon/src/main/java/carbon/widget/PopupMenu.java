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
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

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
        super(LayoutInflater.from(context).inflate(R.layout.carbon_popup, null, false));
        getContentView().setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recycler = (RecyclerView) getContentView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public boolean show(View anchor) {
        mAnchorView = anchor;
        setOnDismissListener(this);

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);

        if (anchor != null) {
            final boolean addGlobalListener = mTreeObserver == null;
            mTreeObserver = anchor.getViewTreeObserver(); // Refresh to latest
            if (addGlobalListener) mTreeObserver.addOnGlobalLayoutListener(this);
        } else {
            return false;
        }

        final Resources res = getContentView().getContext().getResources();
        int maxWidth = res.getDisplayMetrics().widthPixels / 2;

        int margin = (int) res.getDimension(R.dimen.carbon_padding);
        int height = (int) res.getDimension(R.dimen.carbon_toolbarHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);
        setWidth(Math.min(anchor.getWidth() + margin * 2, maxWidth));
        setHeight(marginHalf * 2 + height * 3 + margin * 2);

        setAnimationStyle(0);

        int offset = 0;

        if (anchor instanceof android.widget.TextView) {
            TextView textView = (TextView) anchor;
            String text = textView.getText().toString();
            RecyclerView.Adapter adapter = getAdapter();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).equals(text)) {
                    LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
                    manager.scrollToPositionWithOffset(i, 0);
                    if (i == adapter.getItemCount() - 1) {
                        offset = -height * 2;
                    } else if (i == adapter.getItemCount() - 2) {
                        offset = -height;
                    }
                    break;
                }
            }
        }

        super.showAsDropDown(anchor, -margin, -margin - anchor.getHeight() - marginHalf + offset);

        View content = getContentView().findViewById(R.id.carbon_popupContent);
        content.setVisibility(View.VISIBLE);

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

}
