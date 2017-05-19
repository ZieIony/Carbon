package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface ImageTextSubtextDateItem extends Serializable {
    Drawable getImage();

    String getText();

    String getSubtext();

    String getDate();
}
