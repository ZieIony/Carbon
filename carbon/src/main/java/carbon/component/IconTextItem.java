package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-02-02.
 */
public interface IconTextItem {
    Drawable getIcon(Context context);

    String getText();
}
