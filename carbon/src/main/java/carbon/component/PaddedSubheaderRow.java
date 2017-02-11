package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

/**
 * Created by Marcin on 2017-02-03.
 */
public class PaddedSubheaderRow extends DataBindingRow<AvatarTextItem> {
    public static final RowFactory FACTORY = PaddedSubheaderRow::new;

    public PaddedSubheaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_paddedsubheader);
    }
}
