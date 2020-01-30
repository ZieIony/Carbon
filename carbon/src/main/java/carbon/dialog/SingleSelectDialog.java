package carbon.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.io.Serializable;
import java.util.List;

import carbon.R;
import carbon.component.DataBindingComponent;
import carbon.recycler.RowFactory;
import carbon.view.SelectionMode;
import carbon.widget.RadioButton;

public class SingleSelectDialog<Type extends Serializable> extends ListDialog<Type> {

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
        super.setItems(items, parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_radiotext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (getSelectedItem() == data) {
                    RadioButton radio = getView().findViewById(R.id.carbon_radioButton);
                    radio.setChecked(true);
                }
            }
        });
        adapter.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setItems(List<Type> items) {
        super.setItems(items, parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_radiotext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (getSelectedItem() == data) {
                    RadioButton radio = getView().findViewById(R.id.carbon_radioButton);
                    radio.setChecked(true);
                }
            }
        });
        adapter.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setSelectedItem(Type selectedItem) {
        adapter.selectItem(selectedItem);
    }

    public Type getSelectedItem() {
        return adapter.getSelectedItems().isEmpty() ? null : adapter.getSelectedItems().get(0);
    }
}
