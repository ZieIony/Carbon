package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public interface IconSearchItem extends Serializable {
    Drawable getIcon();

    String getQuery();

    String getHint();
}
