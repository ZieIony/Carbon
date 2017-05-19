package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class ImageTextSubtextDateRow<Type extends ImageTextSubtextDateItem> extends DataBindingComponent<Type> {
    public static final RowFactory FACTORY = ImageTextSubtextDateRow::new;

    public ImageTextSubtextDateRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_imagetextsubtextdate);
    }
}
