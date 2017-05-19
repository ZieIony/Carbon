package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface IconTextItem extends Serializable {
    Drawable getIcon();

    String getText();
}
