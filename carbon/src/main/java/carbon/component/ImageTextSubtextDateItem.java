package carbon.component;

import android.graphics.drawable.Drawable;

public interface ImageTextSubtextDateItem extends ComponentItem{
    Drawable getImage();

    String getText();

    String getSubtext();

    String getDate();
}
