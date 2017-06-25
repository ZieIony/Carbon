package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface AvatarTextRatingSubtextDateItem extends Serializable {
    Drawable getAvatar();

    String getText();

    int getRating();

    String getSubtext();

    String getDate();
}
