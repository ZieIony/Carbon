package carbon.beta;

import android.content.Context;
import android.util.AttributeSet;

import carbon.internal.Roboto;
import carbon.internal.TypefaceUtils;

/**
 * Created by Marcin on 2014-11-07.
 */
public class ToggleButton extends android.widget.ToggleButton {
    public ToggleButton(Context context) {
        super(context);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        if (!isInEditMode())
            setTypeface(TypefaceUtils.getTypeface(getContext(), Roboto.Medium));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text.toString().toUpperCase(), type);
    }
}
