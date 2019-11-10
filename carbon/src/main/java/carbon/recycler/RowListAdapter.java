package carbon.recycler;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.component.Component;
import carbon.component.ItemTransformer;

public class RowListAdapter<Type extends Serializable> extends ListAdapter<RowViewHolder<Type>, Type> {
    private SparseArray<RowDescriptor<? extends Type, ? extends Type>> factories = new SparseArray<>();
    private Map<Class<? extends Type>, Integer> types = new HashMap<>();

    public RowListAdapter() {
    }

    public <ItemType extends Type> RowListAdapter(Class<ItemType> type, RowFactory<ItemType> factory) {
        putFactory(type, factory);
    }

    public <ItemType extends Type> RowListAdapter(List<Type> items, RowFactory<ItemType> factory) {
        super(items);
        putFactory((Class<ItemType>) items.get(0).getClass(), factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowListAdapter(Class<ItemType> type, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
        putFactory(type, transformer, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowListAdapter(List<Type> items, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
        super(items);
        putFactory((Class<ItemType>) items.get(0).getClass(), transformer, factory);
    }

    public <ItemType extends Type> void putFactory(Class<ItemType> type, RowFactory<ItemType> factory) {
        putFactory(type, ItemTransformer.EMPTY, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> void putFactory(Class<ItemType> type, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
        int viewType = types.size();
        factories.put(viewType, new RowDescriptor<>(transformer, factory));
        types.put(type, viewType);
    }

    @Override
    public RowViewHolder<Type> onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Component<Type> component = (Component<Type>) factories.get(viewType).factory.create(viewGroup);
        return new RowViewHolder<>(component);
    }

    @Override
    public void onBindViewHolder(final RowViewHolder<Type> holder, final int position) {
        super.onBindViewHolder(holder, position);
        Type data = getItem(position);
        Component<Type> component = holder.getComponent();
        ItemTransformer transformer = factories.get(getItemViewType(position)).transformer;
        component.bind((Type) transformer.transform(data));
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder<Type> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        Type data = getItem(position);
        Component<Type> component = holder.getComponent();
        ItemTransformer transformer = factories.get(getItemViewType(position)).transformer;
        component.bind((Type) transformer.transform(data));
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(getItem(position).getClass());
    }
}
