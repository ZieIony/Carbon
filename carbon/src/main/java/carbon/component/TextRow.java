package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class TextRow<String> extends DataBindingComponent<String> {

    public TextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_text);
    }
}
