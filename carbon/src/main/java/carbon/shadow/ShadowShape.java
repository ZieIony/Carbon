package carbon.shadow;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import carbon.widget.RoundedCornersView;

public enum ShadowShape {
    RECT, ROUND_RECT, CIRCLE;

    public static ViewOutlineProvider viewOutlineProvider;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewOutlineProvider = new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                @Override
                public void getOutline(View view, Outline outline) {
                    ShadowShape shadowShape = ((ShadowView) view).getShadowShape();
                    if (shadowShape == RECT) {
                        outline.setRect(0, 0, view.getWidth(), view.getHeight());
                    } else if (shadowShape == ROUND_RECT) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), ((RoundedCornersView) view).getCornerRadius());
                    } else if (shadowShape == CIRCLE) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    }
                }
            };
        }
    }
}
