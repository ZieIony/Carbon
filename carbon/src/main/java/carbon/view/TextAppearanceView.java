package carbon.view;

import android.content.res.ColorStateList;
import android.graphics.Typeface;

public interface TextAppearanceView {
    void setTypeface(Typeface typeface, int style);

    void setAllCaps(boolean allCaps);

    void setTextColor(ColorStateList textColor);

    void setTextSize(float size);
}
