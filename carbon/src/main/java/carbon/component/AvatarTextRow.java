package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;

/**
 * Created by Marcin on 2017-02-03.
 */
public class AvatarTextRow extends DataBindingComponent<AvatarTextItem> {
    public static final RowFactory FACTORY = AvatarTextRow::new;

    public AvatarTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_avatartext);
    }
}
