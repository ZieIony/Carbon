package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

public class DividerRow implements Component<DividerItem> {

    private View view;

    public DividerRow(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_row_divider, parent, false);
    }

    @Override
    public View getView() {
        return view;
    }

}
