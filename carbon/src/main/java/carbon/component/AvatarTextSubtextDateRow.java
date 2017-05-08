package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class AvatarTextSubtextDateRow extends DataBindingComponent<AvatarTextItem> {
    public static final RowFactory FACTORY = AvatarTextSubtextDateRow::new;

    public AvatarTextSubtextDateRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_avatartextsubtextdate);
    }
}
