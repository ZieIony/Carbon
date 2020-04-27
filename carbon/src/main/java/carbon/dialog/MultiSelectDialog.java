package carbon.dialog;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.io.Serializable;
import java.util.List;

import carbon.R;
import carbon.component.LayoutComponent;
import carbon.databinding.CarbonRowDialogCheckboxtextBinding;
import carbon.recycler.RowFactory;
import carbon.view.SelectionMode;
import carbon.widget.RecyclerView;

public class MultiSelectDialog<Type extends Serializable> extends ListDialog<Type> {

    private class ItemRow extends LayoutComponent<Type> {

        private final CarbonRowDialogCheckboxtextBinding binding = CarbonRowDialogCheckboxtextBinding.bind(getView());

        ItemRow(ViewGroup parent) {
            super(parent, R.layout.carbon_row_dialog_checkboxtext);
        }

        @Override
        public void bind(Type data) {
            super.bind(data);
            binding.carbonCheckBox.setChecked(getSelectedItems().contains(data));
            binding.carbonText.setText(data.toString());
        }
    }

    public MultiSelectDialog(@NonNull Context context) {
        super(context);
    }

    public MultiSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected RecyclerView.OnItemClickedListener<Type> getInternalListener() {
        return (view, item, position) -> {
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
        super.setItems(items, ItemRow::new);
        adapter.setSelectionMode(SelectionMode.MULTI);
    }

    public void setItems(List<Type> items) {
        super.setItems(items, ItemRow::new);
        adapter.setSelectionMode(SelectionMode.MULTI);
    }

    public void setSelectedItems(List<Type> selectedItems) {
        adapter.setSelectedItems(selectedItems);
    }

    public List<Type> getSelectedItems() {
        return adapter.getSelectedItems();
    }
}
