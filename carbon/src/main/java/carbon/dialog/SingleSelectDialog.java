package carbon.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.component.DataBindingComponent;
import carbon.recycler.RowFactory;
import carbon.widget.RadioButton;
import carbon.widget.RecyclerView;

public class SingleSelectDialog<Type extends Serializable> extends ListDialog<Type> {
    private Type selectedItem;

    public SingleSelectDialog(@NonNull Context context) {
        super(context);
    }

    public SingleSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected RecyclerView.OnItemClickedListener<Type> getInternalListener() {
        return (view, item, position) -> {
            RadioButton radio = view.findViewById(R.id.carbon_radioButton);
            radio.setChecked(true);
            RadioButton prevRadio = recyclerView.findViewHolderForAdapterPosition(items.indexOf(selectedItem)).itemView.findViewById(R.id.carbon_radioButton);
            prevRadio.setChecked(false);
            selectedItem = item;
            if (listener != null)
                listener.onItemClicked(view, item, position);
            dismiss();
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
        super.setItems(items, parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_radiotext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (selectedItem == data) {
                    RadioButton radio = getView().findViewById(R.id.carbon_radioButton);
                    radio.setChecked(true);
                }
            }
        });
    }

    public void setItems(List<Type> items) {
        super.setItems(new ArrayList<>(items), parent -> new DataBindingComponent<Type>(parent, R.layout.carbon_row_dialog_radiotext) {
            @Override
            public void bind(Type data) {
                super.bind(data);
                if (selectedItem == data) {
                    RadioButton radio = getView().findViewById(R.id.carbon_radioButton);
                    radio.setChecked(true);
                }
            }
        });
    }

    public void setSelectedItem(Type selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Type getSelectedItem() {
        return selectedItem;
    }
}
