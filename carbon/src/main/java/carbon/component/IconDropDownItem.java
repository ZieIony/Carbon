package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface IconDropDownItem<Type> extends Serializable {
    Drawable getIcon();

    String getHint();

    Type[] getItems();

    Type getSelectedItem();
}
