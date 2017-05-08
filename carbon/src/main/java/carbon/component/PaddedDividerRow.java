package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class PaddedDividerRow implements Component<DividerItem> {
    public static final RowFactory FACTORY = PaddedDividerRow::new;

    private View view;

    public PaddedDividerRow(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_row_paddeddivider, parent, false);
    }

    @Override
    public View getView() {
        return view;
    }

}
