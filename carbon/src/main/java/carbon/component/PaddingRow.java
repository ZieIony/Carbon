package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

public class PaddingRow implements Component<PaddingItem> {

    private View view;

    public PaddingRow(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_row_padding, parent, false);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void bind(PaddingItem data) {
        view.setMinimumHeight(data.getPadding());
    }
}
