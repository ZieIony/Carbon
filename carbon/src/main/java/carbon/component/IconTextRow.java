package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class IconTextRow<Type extends IconTextItem> extends DataBindingComponent<Type> {

    public IconTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_icontext);
    }
}
