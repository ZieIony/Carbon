package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.widget.TextMarker;

public class AvatarTextRatingSubtextDateRow<Type extends AvatarTextRatingSubtextDateItem> extends DataBindingComponent<Type> {

    public AvatarTextRatingSubtextDateRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_avatartextratingsubtextdate);
    }

    @Override
    public void bind(Type data) {
        super.bind(data);
        TextMarker marker = getView().findViewById(R.id.carbon_marker2);
        marker.setText(data.getSubtext());
    }
}
