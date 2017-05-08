package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class RadioButton extends carbon.widget.RadioButton implements RxViewInterface, RxTextViewInterface, RxCheckableInterface {

    public RadioButton(Context context) {
        super(context);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
