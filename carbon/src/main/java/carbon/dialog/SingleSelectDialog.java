package carbon.dialog;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.io.Serializable;
import java.util.List;

import carbon.R;
import carbon.component.LayoutComponent;
import carbon.databinding.CarbonRowDialogRadiotextBinding;
import carbon.recycler.RowFactory;
import carbon.view.SelectionMode;

public class SingleSelectDialog<Type extends Serializable> extends ListDialog<Type> {

    private class ItemRow extends LayoutComponent<Type> {

        private final CarbonRowDialogRadiotextBinding binding = CarbonRowDialogRadiotextBinding.bind(getView());

        ItemRow(ViewGroup parent) {
            super(parent, R.layout.carbon_row_dialog_radiotext);
        }

        @Override
        public void bind(Type data) {
            binding.carbonRadioButton.setChecked(getSelectedItem() == data);
            binding.carbonText.setText(data.toString());
        }
    }

    public SingleSelectDialog(@NonNull Context context) {
        super(context);
    }

    public SingleSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
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
        adapter.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setItems(List<Type> items) {
        super.setItems(items, ItemRow::new);
        adapter.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setSelectedItem(Type selectedItem) {
        adapter.selectItem(selectedItem);
    }

    public Type getSelectedItem() {
        return adapter.getSelectedItems().isEmpty() ? null : adapter.getSelectedItems().get(0);
    }
}
