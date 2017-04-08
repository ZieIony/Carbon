package carbon.component;

import android.graphics.drawable.Drawable;

public interface IconDropDownItem<Type> extends ComponentItem {
    Drawable getIcon();

    String getHint();

    Type[] getItems();

    Type getSelectedItem();
}
