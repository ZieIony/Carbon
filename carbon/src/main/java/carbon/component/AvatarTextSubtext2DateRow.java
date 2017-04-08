package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

public class AvatarTextSubtext2DateRow extends DataBindingComponent<AvatarTextItem> {
    public static final RowFactory FACTORY = AvatarTextSubtext2DateRow::new;

    public AvatarTextSubtext2DateRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_avatartextsubtext2date);
    }
}
