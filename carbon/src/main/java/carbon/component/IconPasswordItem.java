package carbon.component;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-03-06.
 */

public interface IconPasswordItem extends ComponentItem {
    Drawable getIcon();

    String getHint();

    String getText();
}
