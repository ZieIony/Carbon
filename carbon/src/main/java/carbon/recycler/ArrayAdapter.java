package carbon.recycler;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import carbon.widget.AutoCompleteEditText;

public abstract class ArrayAdapter<VH extends RecyclerView.ViewHolder, I> extends Adapter<VH, I> implements AutoCompleteEditText.AutoCompleteDataProvider<I> {
    private carbon.widget.RecyclerView.OnItemClickedListener<I> onItemClickedListener;
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
    public String[] getItemWords(int position) {
        return new String[]{getItem(position).toString()};
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public void setDiffCallback(DiffArrayCallback<I> diffCallback) {
        this.diffCallback = diffCallback;
    }

    public void setItems(@NonNull I[] items) {
        if (!diff) {
            this.items = items;
            return;
        }
        if (diffCallback == null)
            diffCallback = new DiffArrayCallback<>();
        diffCallback.setArrays(this.items, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.items = items;
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

    protected void fireOnItemClickedEvent(View view, int position) {
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
