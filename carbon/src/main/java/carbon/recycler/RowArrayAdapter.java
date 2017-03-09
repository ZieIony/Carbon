package carbon.recycler;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowArrayAdapter<Type> extends ArrayAdapter<RowViewHolder, Type> {
    private Map<Class, Integer> types = new HashMap<>();
    private List<RowFactory> factories = new ArrayList<>();

    public RowArrayAdapter(Class type, RowFactory factory) {
        addFactory(type, factory);
    }

    public RowArrayAdapter(Type[] items, RowFactory factory) {
        super(items);
        addFactory(items[0].getClass(), factory);
    }

    public void addFactory(Class type, RowFactory factory) {
        types.put(type, types.size());
        factories.add(factory);
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Component component = factories.get(viewType).create(viewGroup);
        RowViewHolder viewHolder = new RowViewHolder(component.getView());
        viewHolder.setComponent(component);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RowViewHolder holder, final int position) {
        Type data = getItem(position);
        Component component = holder.getComponent();
        component.bind(data);
        component.getView().setOnClickListener(view -> fireOnItemClickedEvent(holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(getItem(position).getClass());
    }
}
