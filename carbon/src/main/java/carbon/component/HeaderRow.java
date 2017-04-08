package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class HeaderRow extends DataBindingComponent<AvatarTextItem> {
    public static final RowFactory FACTORY = HeaderRow::new;

    public HeaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_header);
    }
}
