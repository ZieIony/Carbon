package carbon.recycler;

import android.util.SparseArray;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import carbon.component.Component;
import carbon.component.ItemTransformer;

public class RowArrayAdapter<Type extends Serializable> extends ArrayAdapter<RowViewHolder<Type>, Type> {
    private SparseArray<RowDescriptor<? extends Type, ? extends Type>> factories = new SparseArray<>();
    private Map<Class<? extends Type>, Integer> types = new HashMap<>();

    public <ItemType extends Type> RowArrayAdapter(Class<ItemType> type, RowFactory<ItemType> factory) {
        addFactory(type, factory);
    }

    public <ItemType extends Type> RowArrayAdapter(Type[] items, RowFactory<ItemType> factory) {
        super(items);
        addFactory((Class<ItemType>) items[0].getClass(), factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowArrayAdapter(Class<ItemType> type, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
        addFactory(type, transformer, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowArrayAdapter(Type[] items, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
        super(items);
        addFactory((Class<ItemType>) items[0].getClass(), transformer, factory);
    }

    public <ItemType extends Type> void addFactory(Class<ItemType> type, RowFactory<ItemType> factory) {
        addFactory(type, ItemTransformer.EMPTY, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> void addFactory(Class<ItemType> type, ItemTransformer<ItemType, FactoryType> transformer, RowFactory<FactoryType> factory) {
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
        Type data = getItem(position);
        Component<Type> component = holder.getComponent();
        ItemTransformer transformer = factories.get(getItemViewType(position)).transformer;
        component.bind((Type) transformer.transform(data));
        component.getView().setOnClickListener(view -> fireOnItemClickedEvent(component.getView(), holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(getItem(position).getClass());
    }
}
