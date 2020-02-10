package carbon.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.view.SelectionMode;

public abstract class ListAdapter<VH extends RecyclerView.ViewHolder, I> extends Adapter<VH, I> implements SelectableItemsAdapter<I> {
    private carbon.widget.RecyclerView.OnItemClickedListener<I> onItemClickedListener;
    private Map<Class<? extends I>, carbon.widget.RecyclerView.OnItemClickedListener<? extends I>> onItemClickedListeners = new HashMap<>();
    private boolean diff = true;
    private DiffListCallback<I> diffCallback;

    private SelectionMode selectionMode = SelectionMode.NONE;
    private ArrayList<I> selectedItems = new ArrayList<>();

    public ListAdapter() {
        items = new ArrayList<>();
    }

    public ListAdapter(List<I> items) {
        this.items = items;
    }

    protected List<I> items;

    public I getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDiffCallback(DiffListCallback<I> diffCallback) {
        this.diffCallback = diffCallback;
    }

    public void setItems(@NonNull List<I> items) {
        List<I> newItems = new ArrayList<>(items);
        if (!diff) {
            this.items = newItems;
            return;
        }
        if (diffCallback == null)
            diffCallback = new DiffListCallback<>();
        diffCallback.setLists(this.items, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.items = newItems;
        diffResult.dispatchUpdatesTo(this);
        setSelectionMode(selectionMode);
    }

    public List<I> getItems() {
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
        if (position < 0 || position > items.size())
            return;
        I item = items.get(position);
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

    @Override
    public void setSelectionMode(@NonNull SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        setSelectedItems(selectedItems);
    }

    @Override
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    @Override
    public void setSelectedIndices(List<Integer> selectedIndices) {
        setSelectedItems(Stream.of(selectedIndices).map(it -> items.get(it)).toList());
    }

    @Override
    public List<Integer> getSelectedIndices() {
        return Stream.of(selectedItems).map(items::indexOf).toList();
    }

    @Override
    public void setSelectedItems(List<I> selectedItems) {
        ArrayList<I> prevSelectedItems = this.selectedItems;
        this.selectedItems = new ArrayList<>();
        for (I item : prevSelectedItems)
            notifyItemChanged(items.indexOf(item), false);
        if (selectionMode != SelectionMode.NONE) {
            for (I item : selectedItems) {
                int index = items.indexOf(item);
                if (index != -1) {
                    this.selectedItems.add(item);
                    notifyItemChanged(index, true);
                }
            }
        }
    }

    @Override
    public List<I> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public void selectItem(I item) {
        if (selectionMode == SelectionMode.SINGLE) {
            if (selectedItems.size() > 0) {
                int deselectedIndex = items.indexOf(selectedItems.get(0));
                selectedItems.clear();
                notifyItemChanged(deselectedIndex, false);
            }
            int selectedIndex = items.indexOf(item);
            selectedItems.add(item);
            notifyItemChanged(selectedIndex, true);
        } else if (selectionMode == SelectionMode.MULTI) {
            int indexOfSelectedIndex = selectedItems.indexOf(item);
            int selectedIndex = items.indexOf(item);
            if (indexOfSelectedIndex != -1) {
                selectedItems.remove(item);
                notifyItemChanged(selectedIndex, false);
            } else {
                selectedItems.add(item);
                notifyItemChanged(selectedIndex, true);
            }
        }
    }
}
