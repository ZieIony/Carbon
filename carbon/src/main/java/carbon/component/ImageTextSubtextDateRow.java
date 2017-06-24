package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class ImageTextSubtextDateRow<Type extends ImageTextSubtextDateItem> extends DataBindingComponent<Type> {

    public ImageTextSubtextDateRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_imagetextsubtextdate);
    }
}
