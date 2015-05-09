package carbon.widget;

import android.content.res.ColorStateList;

/**
 * Created by Marcin on 2015-05-08.
 */
public interface TintedView {
    void setTint(ColorStateList list);

    void setTint(int color);

    ColorStateList getTint();
}
