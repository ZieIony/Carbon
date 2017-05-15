package carbon.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.component.DataBindingComponent;
import carbon.recycler.RowFactory;
import carbon.widget.CheckBox;
import carbon.widget.RecyclerView;

public class MultiSelectDialog<Type> extends ListDialog<Type> {
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

    protected RecyclerView.OnItemClickedListener getInternalListener() {
        return (view, item, position) -> {
            Type selectedItem = items.get(position);
            if (selectedItems.contains(selectedItem)) {
                selectedItems.remove(selectedItem);
            } else {
                selectedItems.add(selectedItem);
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
        super.setItems(items, parent -> new DataBindingComponent(parent, R.layout.carbon_row_dialog_checkboxtext) {
            @Override
            public void bind(Object data) {
                super.bind(data);
                if (selectedItems.contains(data)) {
                    CheckBox checkBox = (CheckBox) getView().findViewById(R.id.carbon_checkBox);
                    checkBox.setChecked(true);
                }
            }
        });
    }

    public void setSelectedItems(Type[] selectedItems) {
        this.selectedItems.clear();
        Stream.of(selectedItems).forEach(item -> this.selectedItems.add(item));
    }

}
