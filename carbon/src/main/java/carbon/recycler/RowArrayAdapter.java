package carbon.recycler;

import android.view.ViewGroup;

import carbon.widget.RecyclerView;

public class RowArrayAdapter<Type> extends RecyclerView.ArrayAdapter<RowViewHolder, Type> {
    private RowFactory factory;

    public RowArrayAdapter(RowFactory factory) {
        this.factory = factory;
    }

    public RowArrayAdapter(Type[] items, RowFactory factory) {
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
