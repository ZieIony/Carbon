package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class PaddedHeaderRow<Type extends DefaultHeaderItem> extends DataBindingComponent<Type> {

    public PaddedHeaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_paddedheader);
    }
}
