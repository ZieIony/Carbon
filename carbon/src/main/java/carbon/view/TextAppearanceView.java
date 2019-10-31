package carbon.view;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextPaint;

import androidx.annotation.NonNull;

public interface TextAppearanceView {
    void setTypeface(Typeface typeface, int style);

    void setAllCaps(boolean allCaps);

    void setTextColor(@NonNull ColorStateList textColor);

    void setTextSize(float size);

    CharSequence getText();

    TextPaint getPaint();

    void setText(CharSequence text);
}
