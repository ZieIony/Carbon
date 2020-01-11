package carbon.recycler;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class DragTouchHelper<T> extends ItemTouchHelper.SimpleCallback {

    public interface OnItemMovedListener<Type> {
        boolean onItemMoved(Type item, int position, int targetPosition);
    }

    private final ItemTouchHelper itemTouchHelper;
    private RecyclerView recycler;
    private Adapter<?, T> adapter;
    private OnItemMovedListener<T> onItemMovedListener;
    private HashMap<Class<? extends T>, OnItemMovedListener<? extends T>> onItemMovedListeners = new HashMap<>();

    public DragTouchHelper(RecyclerView recycler, Adapter<?, T> adapter) {
        super(0, 0);
        this.recycler = recycler;
        this.adapter = adapter;
        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    public void setOnItemMovedListener(OnItemMovedListener<T> onItemMovedListener) {
        this.onItemMovedListener = onItemMovedListener;
    }

    public <ItemType extends T> void setOnItemMovedListener(Class<ItemType> type, OnItemMovedListener<ItemType> onItemMovedListener) {
        onItemMovedListeners.put(type, onItemMovedListener);
    }

    @Override
    public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        T item = adapter.getItem(viewHolder.getAdapterPosition());
        if (onItemMovedListener != null || (item != null && onItemMovedListeners.containsKey(item.getClass()))) {
            return ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        } else {
            return super.getDragDirs(recyclerView, viewHolder);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        T item = adapter.getItem(viewHolder.getAdapterPosition());
        if (item != null) {
            OnItemMovedListener<T> onItemMovedListener = (OnItemMovedListener<T>) onItemMovedListeners.get(item.getClass());
            if (onItemMovedListener != null)
                return onItemMovedListener.onItemMoved(item, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        if (onItemMovedListener != null)
            return onItemMovedListener.onItemMoved(adapter.getItem(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    public void startDrag(View view) {
        itemTouchHelper.startDrag(recycler.findContainingViewHolder(view));
    }
}
