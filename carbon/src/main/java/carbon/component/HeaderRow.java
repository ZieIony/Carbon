package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class HeaderRow<Type extends DefaultHeaderItem> extends DataBindingComponent<Type> {

    public HeaderRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_header);
    }
}
