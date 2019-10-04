package carbon.recycler;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffListCallback<T> extends DiffUtil.Callback {
    protected List<T> items, newItems;

    public void setLists(List<T> items, List<T> newItems) {
        this.items = items;
        this.newItems = newItems;
    }

    public List<T> getItems() {
        return items;
    }

    public List<T> getNewItems() {
        return newItems;
    }

    @Override
    public int getOldListSize() {
        return items.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return items.get(oldItemPosition) == newItems.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return items.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }
}
