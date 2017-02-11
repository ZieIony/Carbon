package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

/**
 * Created by Marcin on 2017-02-03.
 */
public class SubheaderRow extends DataBindingRow<AvatarTextItem> {
    public static final RowFactory FACTORY = SubheaderRow::new;

    public SubheaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_subheader);
    }
}
