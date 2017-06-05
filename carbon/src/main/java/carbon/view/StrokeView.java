package carbon.view;

import android.content.res.ColorStateList;

public interface StrokeView {
    void setStroke(ColorStateList colorStateList);

    void setStroke(int color);

    ColorStateList getStroke();

    void setStrokeWidth(float strokeWidth);

    float getStrokeWidth();

}
