package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface AvatarTextSubtextDateItem extends Serializable {
    Drawable getAvatar();

    String getText();

    String getSubtext();

    String getDate();
}
