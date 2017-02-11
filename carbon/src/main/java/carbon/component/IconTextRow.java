package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

/**
 * Created by Marcin on 2017-02-03.
 */
public class IconTextRow extends DataBindingRow<IconTextItem> {
    public static final RowFactory FACTORY = IconTextRow::new;

    public IconTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_icontext);
    }
}
