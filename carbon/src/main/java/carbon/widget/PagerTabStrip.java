package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

/**
 * @deprecated Class renamed to {@link TabLayout}
 */
@Deprecated
public class PagerTabStrip extends TabLayout {
    public PagerTabStrip(Context context) {
        super(context);
    }

    public PagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerTabStrip(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PagerTabStrip(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
