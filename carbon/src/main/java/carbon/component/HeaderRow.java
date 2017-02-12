package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

/**
 * Created by Marcin on 2017-02-03.
 */
public class HeaderRow extends DataBindingRow<AvatarTextItem> {
    public static final RowFactory FACTORY = HeaderRow::new;

    public HeaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_header);
    }
}
