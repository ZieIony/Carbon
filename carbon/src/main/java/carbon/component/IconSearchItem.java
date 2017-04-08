package carbon.component;

import android.graphics.drawable.Drawable;

public interface IconSearchItem extends ComponentItem {
    Drawable getIcon();

    String getQuery();

    String getHint();
}
