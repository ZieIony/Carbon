package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class DividerRow implements Component<DividerItem> {
    public static final RowFactory FACTORY = DividerRow::new;

    private View view;

    public DividerRow(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_row_divider, parent, false);
    }

    @Override
    public View getView() {
        return view;
    }

}
