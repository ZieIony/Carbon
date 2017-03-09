package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class CheckBoxRow extends DataBindingComponent<CheckBoxItem> {
    public static final RowFactory FACTORY = CheckBoxRow::new;

    public CheckBoxRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_iconcheckbox);
    }
}
