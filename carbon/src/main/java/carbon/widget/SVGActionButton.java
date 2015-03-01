package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import carbon.R;
import carbon.animation.ElevationStateAnimator;

/**
 * Created by Marcin on 2014-12-04.
 */
public class SVGActionButton extends SVGView {

    public SVGActionButton(Context context) {
        this(context, null);
    }

    public SVGActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_fabStyle);
    }

    public SVGActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setClickable(true);
        setRect(false);
        addStateAnimator(new ElevationStateAnimator(this));
    }

}
