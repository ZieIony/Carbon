package carbon.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.view.SelectionMode;

public abstract class ArrayAdapter<VH extends RecyclerView.ViewHolder, I> extends Adapter<VH, I> implements SelectableItemsAdapter<I> {
    private carbon.widget.RecyclerView.OnItemClickedListener<I> onItemClickedListener;
    private Map<Class<? extends I>, carbon.widget.RecyclerView.OnItemClickedListener<? extends I>> onItemClickedListeners = new HashMap<>();
    private boolean diff = true;
    private DiffArrayCallback<I> diffCallback;

    private SelectionMode selectionMode = SelectionMode.NONE;
    private ArrayList<I> selectedItems;

    public ArrayAdapter() {
        items = (I[]) new Object[0];    // doesn't really matter
    }

    public ArrayAdapter(I[] items) {
        this.items = items;
    }

    protected I[] items;

    public I getItem(int position) {
        return items[position];
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public void setDiffCallback(DiffArrayCallback<I> diffCallback) {
        this.diffCallback = diffCallback;
    }

    public void setItems(@NonNull I[] items) {
        setSelectionMode(selectionMode);
        I[] newItems = Arrays.copyOf(items, items.length);
        if (!diff) {
            this.items = newItems;
            return;
        }
        if (diffCallback == null)
            diffCallback = new DiffArrayCallback<>();
        diffCallback.setArrays(this.items, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.items = newItems;
        diffResult.dispatchUpdatesTo(this);
    }

    public I[] getItems() {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickedListener(carbon.widget.RecyclerView.OnItemClickedListener<I> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setOnItemClickedListener(carbon.widget.RecyclerView.OnItemClickedListener2<I> onItemClickedListener) {
        this.onItemClickedListener = (view, item, position) -> {
            if (onItemClickedListener != null)
                onItemClickedListener.onItemClicked(item);
        };
    }

    public <ItemType extends I> void setOnItemClickedListener(Class<ItemType> type, carbon.widget.RecyclerView.OnItemClickedListener<ItemType> onItemClickedListener) {
        this.onItemClickedListeners.put(type, onItemClickedListener);
    }

    public <ItemType extends I> void setOnItemClickedListener(Class<ItemType> type, carbon.widget.RecyclerView.OnItemClickedListener2<ItemType> onItemClickedListener) {
        this.onItemClickedListeners.put(type, (carbon.widget.RecyclerView.OnItemClickedListener<ItemType>) (view, item, position) -> {
            if (onItemClickedListener != null)
                onItemClickedListener.onItemClicked(item);
        });
    }

    protected void fireOnItemClickedEvent(View view, int position) {
        if (position < 0 || position > items.length)
            return;
        I item = items[position];
        carbon.widget.RecyclerView.OnItemClickedListener<I> typeSpecificListener = (carbon.widget.RecyclerView.OnItemClickedListener<I>) onItemClickedListeners.get(item.getClass());
        if (typeSpecificListener != null)
            typeSpecificListener.onItemClicked(view, item, position);
        if (onItemClickedListener != null)
            onItemClickedListener.onItemClicked(view, item, position);
        if (selectionMode != SelectionMode.NONE && view.isFocusable() && view.isClickable())
            selectItem(item);
    }

    public void setDiffEnabled(boolean useDiff) {
        this.diff = useDiff;
    }

    public boolean isDiffEnabled() {
        return diff;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(view -> fireOnItemClickedEvent(holder.itemView, holder.getAdapterPosition()));
    }

    public void setSelectionMode(@NonNull SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        if (selectionMode == SelectionMode.NONE) {
            selectedItems = null;
        } else {
            selectedItems = new ArrayList<>();
        }
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectedIndices(List<Integer> selectedIndices) {
        this.selectedItems.clear();
        for (int index : selectedIndices)
            selectedItems.add(items[index]);
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIndices() {
        ArrayList<Integer> selectedIndices = new ArrayList<>();
        for (I item : selectedItems)
            selectedIndices.add(indexOf(item));
        return selectedIndices;
    }

    public void setSelectedItems(List<I> selectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
        notifyDataSetChanged();
    }

    public List<I> getSelectedItems() {
        return selectedItems;
    }

    public void selectItem(I item) {
        if (selectionMode == SelectionMode.SINGLE) {
            if (selectedItems.size() > 0) {
                int deselectedIndex = indexOf(selectedItems.get(0));
                selectedItems.clear();
                notifyItemChanged(deselectedIndex, false);
            }
            int selectedIndex = indexOf(item);
            selectedItems.add(item);
            notifyItemChanged(selectedIndex, true);
        } else if (selectionMode == SelectionMode.MULTI) {
            int indexOfSelectedIndex = selectedItems.indexOf(item);
            int selectedIndex = indexOf(item);
            if (indexOfSelectedIndex != -1) {
                selectedItems.remove(item);
                notifyItemChanged(selectedIndex, false);
            } else {
                selectedItems.add(item);
                notifyItemChanged(selectedIndex, true);
            }
        }
    }

    private int indexOf(I item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item)
                return i;
        }
        return -1;
    }
}
