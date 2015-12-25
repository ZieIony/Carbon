package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import carbon.R;

/**
 * Created by Marcin on 2015-02-12.
 * <p/>
 * This class is just a LinearLayout with a dedicated style making it look like a
 * real CardView. To make a CardView of one of the other layouts you have to
 * set it's corner radius to 2dp, add a shadow and a margin.
 */
public class CardView extends LinearLayout {

    public CardView(Context context) {
        super(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
