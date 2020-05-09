package carbon.recycler;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.component.Component;
import carbon.component.ItemTransformer;
import carbon.view.SelectionMode;

public class RowListAdapter<Type> extends ListAdapter<RowViewHolder<Type>, Type> {
    private SparseArray<RowDescriptor<? extends Type, ? extends Type>> factories = new SparseArray<>();
    private Map<Class<? extends Type>, Integer> types = new HashMap<>();

    public RowListAdapter() {
    }

    public <ItemType extends Type> RowListAdapter(@NonNull Class<ItemType> type, @NonNull RowFactory<ItemType> factory) {
        putFactory(type, factory);
    }

    public <ItemType extends Type> RowListAdapter(@NonNull List<ItemType> items, @NonNull RowFactory<ItemType> factory) {
        super(new ArrayList<>(items));
        putFactory((Class<ItemType>) items.get(0).getClass(), factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowListAdapter(@NonNull Class<ItemType> type, @NonNull ItemTransformer<ItemType, FactoryType> transformer, @NonNull RowFactory<FactoryType> factory) {
        putFactory(type, transformer, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> RowListAdapter(@NonNull List<ItemType> items, @NonNull ItemTransformer<ItemType, FactoryType> transformer, @NonNull RowFactory<FactoryType> factory) {
        super(new ArrayList<>(items));
        putFactory((Class<ItemType>) items.get(0).getClass(), transformer, factory);
    }

    public <ItemType extends Type> void putFactory(@NonNull Class<ItemType> type, @NonNull RowFactory<ItemType> factory) {
        putFactory(type, ItemTransformer.EMPTY, factory);
    }

    public <ItemType extends Type, FactoryType extends Type> void putFactory(@NonNull Class<ItemType> type, @NonNull ItemTransformer<ItemType, FactoryType> transformer, @NonNull RowFactory<FactoryType> factory) {
        int viewType = types.containsKey(type) ? types.get(type) : types.size();
        factories.put(viewType, new RowDescriptor<>(transformer, factory));
        types.put(type, viewType);
    }

    @Override
    public RowViewHolder<Type> onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        Component<Type> component = (Component<Type>) factories.get(viewType).factory.create(viewGroup);
        return new RowViewHolder<>(component);
    }

    @Override
    public void onBindViewHolder(@NotNull final RowViewHolder<Type> holder, final int position) {
        super.onBindViewHolder(holder, position);
        Type data = getItem(position);
        Component<Type> component = holder.getComponent();
        ItemTransformer transformer = factories.get(getItemViewType(position)).transformer;
        component.bind((Type) transformer.transform(data));
        if (getSelectionMode() != SelectionMode.NONE)
            component.getView().setSelected(getSelectedIndices().contains(position));
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder<Type> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position);
        Type data = getItem(position);
        Component<Type> component = holder.getComponent();
        ItemTransformer transformer = factories.get(getItemViewType(position)).transformer;
        component.bind((Type) transformer.transform(data));
        if (getSelectionMode() != SelectionMode.NONE)
            component.getView().setSelected(getSelectedIndices().contains(position));
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(getItem(position).getClass());
    }
}
