package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface IconPasswordItem extends Serializable {
    Drawable getIcon();

    String getHint();

    String getText();
}
