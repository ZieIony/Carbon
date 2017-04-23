package carbon.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import java.util.List;

import carbon.R;
import carbon.component.DataBindingComponent;
import carbon.recycler.RowFactory;
import carbon.widget.RadioButton;
import carbon.widget.RecyclerView;

public class SingleSelectDialog<Type> extends ListDialog<Type> {
    private Type selectedItem;
    private RecyclerView.OnItemClickedListener listener;

    public SingleSelectDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public SingleSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        super.setOnItemClickedListener((view, position) -> {
            RadioButton radio = (RadioButton) view.findViewById(R.id.carbon_radioButton);
            radio.setChecked(true);
            RadioButton prevRadio = (RadioButton) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.carbon_radioButton);
            prevRadio.setChecked(false);
            selectedItem = items.get(position);
            if (listener != null)
                listener.onItemClicked(view, position);
        });
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
        super.setItems(items, parent -> new DataBindingComponent(parent, R.layout.carbon_row_dialog_radiotext) {
            @Override
            public void bind(Object data) {
                super.bind(data);
                if (selectedItem == data) {
                    RadioButton radio = (RadioButton) getView().findViewById(R.id.carbon_radioButton);
                    radio.setCheckedImmediate(true);
                }
            }
        });
    }

    public void setSelectedItem(Type selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener listener) {
        this.listener = listener;
    }
}
