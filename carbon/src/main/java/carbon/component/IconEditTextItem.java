package carbon.component;

import android.graphics.drawable.Drawable;

public interface IconEditTextItem extends ComponentItem {
    Drawable getIcon();

    String getHint();

    String getText();
}
