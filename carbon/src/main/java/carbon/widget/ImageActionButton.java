package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import carbon.R;
import carbon.animation.ElevationStateAnimator;
import carbon.animation.RippleStateAnimator;
import carbon.drawable.RippleView;
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-12-04.
 */
public class ImageActionButton extends ImageView {

    public ImageActionButton(Context context) {
        super(context, null, R.attr.carbon_fabStyle);
        init(null, R.attr.carbon_fabStyle);
    }

    public ImageActionButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_fabStyle);
        init(attrs, R.attr.carbon_fabStyle);
    }

    public ImageActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setClickable(true);
        setRect(false);
        addStateAnimator(new ElevationStateAnimator(this));
    }

}
