package carbon.component;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-02-03.
 */

public interface AvatarTextSubtextDateItem extends ComponentItem{
    Drawable getAvatar();

    String getText();

    String getSubtext();

    String getDate();
}
