package carbon.component;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-03-06.
 */

public interface IconDropDownItem<Type> extends ComponentItem {
    Drawable getIcon();

    String getHint();

    Type[] getItems();

    Type getSelectedItem();
}
