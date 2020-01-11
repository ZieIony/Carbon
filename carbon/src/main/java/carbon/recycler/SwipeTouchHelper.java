package carbon.recycler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class SwipeTouchHelper<T> extends ItemTouchHelper.SimpleCallback {

    public interface OnItemSwipedListener<Type> {
        void onItemSwiped(Type item, int position);
    }

    public interface OnItemSwipedListener2<Type> {
        void onItemSwiped(Type item);
    }

    private Adapter<?, T> adapter;
    private OnItemSwipedListener<T> onItemSwipedListener;
    private HashMap<Class<? extends T>, OnItemSwipedListener<? extends T>> onItemSwipedListeners = new HashMap<>();

    public SwipeTouchHelper(RecyclerView recycler, Adapter<?, T> adapter) {
        super(0, 0);
        this.adapter = adapter;
        new ItemTouchHelper(this).attachToRecyclerView(recycler);
    }

    public void setOnItemSwipedListener(OnItemSwipedListener<T> onItemSwipedListener) {
        this.onItemSwipedListener = onItemSwipedListener;
    }

    public void setOnItemSwipedListener(OnItemSwipedListener2<T> onItemSwipedListener) {
        this.onItemSwipedListener = (item, position) -> onItemSwipedListener.onItemSwiped(item);
    }

    public <ItemType extends T> void setOnItemSwipedListener(Class<ItemType> type, OnItemSwipedListener<ItemType> onItemSwipedListener) {
        onItemSwipedListeners.put(type, onItemSwipedListener);
    }

    public <ItemType extends T> void setOnItemSwipedListener(Class<ItemType> type, OnItemSwipedListener2<ItemType> onItemSwipedListener) {
        this.onItemSwipedListeners.put(type, (OnItemSwipedListener<ItemType>) (item, position) -> onItemSwipedListener.onItemSwiped(item));
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        T item = adapter.getItem(viewHolder.getAdapterPosition());
        if (onItemSwipedListener != null || (item != null && onItemSwipedListeners.containsKey(item.getClass()))) {
            return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            return super.getSwipeDirs(recyclerView, viewHolder);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        T item = adapter.getItem(viewHolder.getAdapterPosition());
        if (item != null) {
            OnItemSwipedListener<T> onItemSwipedListener = (OnItemSwipedListener<T>) onItemSwipedListeners.get(item.getClass());
            if (onItemSwipedListener != null) {
                onItemSwipedListener.onItemSwiped(item, viewHolder.getAdapterPosition());
                return;
            }
        }
        if (onItemSwipedListener != null)
            onItemSwipedListener.onItemSwiped(adapter.getItem(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
    }
}
