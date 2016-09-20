package carbon.recycler;

import android.view.ViewGroup;

import java.util.List;

import carbon.widget.RecyclerView;

/**
 * Created by Marcin on 2016-06-10.
 */

public class RowListAdapter<Type> extends RecyclerView.ListAdapter<RowViewHolder, Type> {
    private RowFactory factory;

    public RowListAdapter(RowFactory factory) {
        this.factory = factory;
    }

    public RowListAdapter(List<Type> items, RowFactory factory) {
        super(items);
        this.factory = factory;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Row row = factory.create(viewGroup);
        RowViewHolder viewHolder = new RowViewHolder(row.getView());
        viewHolder.setRow(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RowViewHolder holder, final int position) {
        Type data = getItem(position);
        Row row = holder.getRow();
        row.bind(data);
        row.getView().setOnClickListener(view -> fireOnItemClickedEvent(holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int arg0) {
        return 0;
    }


}

