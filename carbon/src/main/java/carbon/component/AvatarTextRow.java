package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class AvatarTextRow<Type extends AvatarTextItem> extends DataBindingComponent<Type> {

    public AvatarTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_avatartext);
    }
}
