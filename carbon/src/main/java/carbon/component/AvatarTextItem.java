package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-02-03.
 */

public interface AvatarTextItem {
    Drawable getAvatar(Context context);

    String getText();
}
