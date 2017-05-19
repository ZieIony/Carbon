package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface AvatarTextItem extends Serializable {
    Drawable getAvatar();

    String getText();
}
