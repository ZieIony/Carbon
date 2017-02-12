package carbon.widget;

import android.content.res.ColorStateList;

/**
 * Created by Marcin on 2017-02-12.
 */
public interface StrokeView {
    void setStroke(ColorStateList colorStateList);

    void setStroke(int color);

    ColorStateList getStroke();

    void setStrokeWidth(float strokeWidth);

    float getStrokeWidth();

}
