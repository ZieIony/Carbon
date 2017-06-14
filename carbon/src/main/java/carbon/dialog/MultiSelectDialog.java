package carbon.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.component.DataBindingComponent;
import carbon.recycler.RowFactory;
import carbon.widget.CheckBox;
import carbon.widget.RecyclerView;

public class MultiSelectDialog<Type extends Serializable> extends ListDialog<Type> {
    private List<Type> selectedItems = new ArrayList<>();

    public MultiSelectDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MultiSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        setPositiveButton("ok", null);
    }

    protected RecyclerView.OnItemClickedListener<Type> getInternalListener() {
        return (view, item, position) -> {
            CheckBox checkBox = recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.carbon_checkBox);
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                checkBox.setChecked(false);
            } else {
                selectedItems.add(item);
                checkBox.setChecked(true);
            }
            if (listener != null)
                listener.onItemClicked(view, item, position);
        };
    }

    @Override
    public void setItems(Type[] items, RowFactory<Type> factory) {
        throw new RuntimeException("not supported");
    }

    @Override
    public void setItems(List<Type> items, RowFactory<Type> factory) {
        throw new RuntimeException("not supported");
    }

    public void setItems(Type[] items) {
        super.setItems(items, parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_checkboxtext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (selectedItems.contains(data)) {
                    CheckBox checkBox = getView().findViewById(R.id.carbon_checkBox);
                    checkBox.setChecked(true);
                }
            }
        });
    }

    public void setItems(List<Type> items) {
        super.setItems(items, parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_checkboxtext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (selectedItems.contains(data)) {
                    CheckBox checkBox = getView().findViewById(R.id.carbon_checkBox);
                    checkBox.setChecked(true);
                }
            }
        });
    }

    public void setSelectedItems(List<Type> selectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
    }

    public List<Type> getSelectedItems() {
        return selectedItems;
    }
}
