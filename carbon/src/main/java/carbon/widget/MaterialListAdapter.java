package carbon.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Marcin on 2014-12-31.
 */
public class MaterialListAdapter implements android.widget.ListAdapter {
    private final ListAdapter adapter;

    public MaterialListAdapter(ListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return adapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return adapter.isEnabled(position);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return adapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View view = adapter.getView(position,convertView,parent);
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)parent).performItemClick(view,position,getItemId(position));
            }
        });
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return adapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return adapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }
}
