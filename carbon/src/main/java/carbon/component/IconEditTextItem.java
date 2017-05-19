package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface IconEditTextItem extends Serializable {
    Drawable getIcon();

    String getHint();

    String getText();
}
