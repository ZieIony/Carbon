package carbon.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.DefaultColorControlStateList;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.ListAdapter;
import carbon.widget.DropDown;
import carbon.widget.FrameLayout;
import carbon.widget.RecyclerView;

public class DropDownMenu extends PopupWindow {

    protected RecyclerView recycler;
    private View anchorView;
    private DropDown.Mode mode;
    private ListAdapter defaultAdapter;
    private DropDown.Style style;
    private List<Integer> selectedIndices = new ArrayList<>();
    private RecyclerView.OnItemClickedListener<Serializable> onItemClickedListener;
    private Serializable customItem;

    public DropDownMenu(Context context) {
        super(View.inflate(context, R.layout.carbon_popupmenu, null));
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
        Drawable dividerDrawable = new ColorStateListDrawable(new DefaultColorControlStateList(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable, context.getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight));
        dividerItemDecoration.setDrawAfter(position -> getAdapter().getItem(position) == customItem);
        recycler.addItemDecoration(dividerItemDecoration);

        defaultAdapter = new DropDown.Adapter<>();
        recycler.setAdapter(defaultAdapter);

        setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(0);
    }

    public boolean show(View anchor) {
        anchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.animateVisibility(View.VISIBLE);

        return true;
    }

    public boolean showImmediate(View anchor) {
        anchorView = anchor;

        super.showAtLocation(anchor, Gravity.START | Gravity.TOP, 0, 0);

        update();

        FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.setVisibility(View.VISIBLE);

        return true;
    }

    public void update() {
        if (anchorView == null)
            return;

        setClippingEnabled(mode == DropDown.Mode.Fit);

        final Resources res = getContentView().getContext().getResources();

        int margin = (int) res.getDimension(R.dimen.carbon_margin);
        int itemHeight = (int) res.getDimension(R.dimen.carbon_listItemHeight);
        int marginHalf = (int) res.getDimension(R.dimen.carbon_paddingHalf);

        int selectedItem = 0;
        ListAdapter adapter = getAdapter();

        if (anchorView instanceof android.widget.TextView) {
            TextView textView = (TextView) anchorView;
            String text = textView.getText().toString();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.getItem(i).toString().equals(text)) {
                    selectedItem = i;
                    break;
                }
            }
        }

        Rect windowRect = new Rect();
        anchorView.getWindowVisibleDisplayFrame(windowRect);
        int hWindow = windowRect.bottom - windowRect.top;
        int wWindow = windowRect.right - windowRect.left;

        int[] location = new int[2];
        anchorView.getLocationInWindow(location);

        if (mode == DropDown.Mode.Over) {
            int maxHeightAbove = location[1] - windowRect.top - marginHalf * 2;
            int maxItemsAbove = maxHeightAbove / itemHeight;
            int maxHeightBelow = hWindow - location[1] - marginHalf * 2;
            int maxItemsBelow = Math.max(1, maxHeightBelow / itemHeight);

            int itemsBelow = Math.min(adapter.getItemCount() - selectedItem, maxItemsBelow);
            int itemsAbove = Math.min(selectedItem, maxItemsAbove);

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - itemsAbove * itemHeight - (itemHeight - (anchorView.getHeight() - anchorView.getPaddingTop() -
                    anchorView.getPaddingBottom())) / 2 + anchorView.getPaddingTop();
            int popupWidth = anchorView.getWidth() + margin * 2 + marginHalf * 2 - anchorView.getPaddingLeft() - anchorView.getPaddingRight();
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

            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            manager.scrollToPositionWithOffset(selectedItem - itemsAbove, 0);

            update(popupX, popupY, popupWidth, popupHeight);
        } else {
            int maxItems = (hWindow - marginHalf * 2 - margin * 2) / itemHeight;

            int popupX = location[0] - margin - marginHalf;
            int popupY = location[1] - marginHalf * 2 - (itemHeight - (anchorView.getHeight() - anchorView.getPaddingTop() - anchorView.getPaddingBottom())) / 2 + anchorView.getPaddingTop();
            int popupWidth = anchorView.getWidth() + margin * 2 + marginHalf * 2 - anchorView.getPaddingLeft() - anchorView.getPaddingRight();
            int popupHeight = marginHalf * 4 + Math.min(recycler.getAdapter().getItemCount(), maxItems) * itemHeight;

            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            manager.scrollToPosition(selectedItem);

            update(popupX, popupY, popupWidth, popupHeight);
        }

        super.update();
    }

    @Override
    public void dismiss() {
        FrameLayout content = getContentView().findViewById(R.id.carbon_popupContainer);
        content.animateVisibility(View.INVISIBLE).addListener(new AnimatorListenerAdapter() {
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

    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener<Serializable> listener) {
        this.onItemClickedListener = listener;
        getAdapter().setOnItemClickedListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            recycler.setAdapter(defaultAdapter);
        } else {
            recycler.setAdapter(adapter);
        }
    }

    public ListAdapter<?, Serializable> getAdapter() {
        return (ListAdapter<?, Serializable>) recycler.getAdapter();
    }

    public void setSelectedIndex(int index) {
        selectedIndices.clear();
        selectedIndices.add(index);
    }

    public void setSelectedIndices(int[] indices) {
        selectedIndices.clear();
        for (int i : indices)
            selectedIndices.add(i);
    }

    public int getSelectedIndex() {
        return selectedIndices.isEmpty() ? Carbon.INVALID_INDEX : selectedIndices.get(0);
    }

    public int[] getSelectedIndices() {
        int[] result = new int[selectedIndices.size()];
        for (int i = 0; i < selectedIndices.size(); i++)
            result[i] = selectedIndices.get(i);
        return result;
    }

    public <Type extends Serializable> void setSelectedItems(List<Type> items) {
        List<Serializable> adapterItems = getAdapter().getItems();
        selectedIndices.clear();
        for (Serializable item : items) {
            for (int i = 0; i < adapterItems.size(); i++) {
                if (adapterItems.get(i).equals(item)) {
                    selectedIndices.add(i);
                    break;
                }
            }
        }
    }

    public <Type extends Serializable> Type getSelectedItem() {
        return selectedIndices.isEmpty() ? null : (Type) getAdapter().getItem(selectedIndices.get(0));
    }

    public <Type extends Serializable> List<Type> getSelectedItems() {
        List<Type> result = new ArrayList<>();
        for (int i : selectedIndices)
            result.add((Type) getAdapter().getItem(i));
        return result;
    }

    public String getSelectedText() {
        if (selectedIndices.isEmpty())
            return "";
        StringBuilder builder = new StringBuilder();
        Collections.sort(selectedIndices);
        for (int i : selectedIndices) {
            builder.append(getAdapter().getItem(i).toString());
            builder.append(", ");
        }
        return builder.substring(0, builder.length() - 2);
    }

    public void toggle(int position) {
        if (selectedIndices.contains(position)) {
            selectedIndices.remove(selectedIndices.indexOf(position));
        } else {
            selectedIndices.add(position);
        }
        androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder = recycler.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof Checkable)
            ((Checkable) viewHolder).toggle();
    }

    public DropDown.Mode getMode() {
        return mode;
    }

    public void setMode(DropDown.Mode mode) {
        this.mode = mode;
    }

    public DropDown.Style getStyle() {
        return style;
    }

    public void setStyle(@NonNull DropDown.Style style) {
        this.style = style;
        ListAdapter<?, Serializable> newAdapter;
        if (style == DropDown.Style.MultiSelect) {
            newAdapter = new DropDown.CheckableAdapter<>(selectedIndices);
        } else {
            newAdapter = new DropDown.Adapter<>();
        }
        if (recycler.getAdapter() == defaultAdapter)
            recycler.setAdapter(newAdapter);
        defaultAdapter = newAdapter;
        newAdapter.setOnItemClickedListener(onItemClickedListener);
    }

    public <Type extends Serializable> void setCustomItem(Type item) {
        if (getAdapter().getItems().get(0) == customItem) {
            getAdapter().getItems().remove(0);
            getAdapter().notifyItemRemoved(0);
        }
        if (getAdapter().getItems().contains(item) || style != DropDown.Style.Editable)
            return;
        customItem = item;
        if (item != null) {
            getAdapter().getItems().add(0, customItem);
            getAdapter().notifyItemInserted(0);
        }
    }

    public <Type extends Serializable> void setItems(List<Type> items) {
        defaultAdapter.setItems(items);
        defaultAdapter.notifyDataSetChanged();
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
