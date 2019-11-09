package carbon.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class ArrayAdapter<VH extends RecyclerView.ViewHolder, I> extends Adapter<VH, I> {
    private carbon.widget.RecyclerView.OnItemClickedListener<I> onItemClickedListener;
    private Map<Class<? extends I>, carbon.widget.RecyclerView.OnItemClickedListener<? extends I>> onItemClickedListeners = new HashMap<>();
    private boolean diff = true;
    private DiffArrayCallback<I> diffCallback;

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

    public <ItemType extends I> void setOnItemClickedListener(Class<ItemType> type, carbon.widget.RecyclerView.OnItemClickedListener<ItemType> onItemClickedListener) {
        this.onItemClickedListeners.put(type, onItemClickedListener);
    }

    protected void fireOnItemClickedEvent(View view, int position) {
        I item = items[position];
        carbon.widget.RecyclerView.OnItemClickedListener<I> typeSpecificListener = (carbon.widget.RecyclerView.OnItemClickedListener<I>) onItemClickedListeners.get(item.getClass());
        if (typeSpecificListener != null) {
            typeSpecificListener.onItemClicked(view, item, position);
        }
        if (onItemClickedListener != null)
            onItemClickedListener.onItemClicked(view, items[position], position);
    }

    public void setDiffEnabled(boolean useDiff) {
        this.diff = useDiff;
    }

    public boolean isDiffEnabled() {
        return diff;
    }
}
