package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;

/**
 * Created by Marcin on 2015-12-18.
 */
public class TableLayout extends FrameLayout {
    public TableLayout(Context context) {
        super(context);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTableLayout();
    }

    private void initTableLayout(){
        View.inflate(getContext(), R.layout.carbon_tablelayout,this);
    }
}
