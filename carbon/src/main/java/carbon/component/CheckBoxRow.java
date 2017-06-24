package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class CheckBoxRow extends DataBindingComponent<CheckBoxItem> {

    public CheckBoxRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_iconcheckbox);
    }
}
