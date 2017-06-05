package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import carbon.widget.RelativeLayout;

public class DependencyLayout extends RelativeLayout {

    public DependencyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
