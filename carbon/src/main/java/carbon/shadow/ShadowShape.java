package carbon.shadow;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import carbon.Carbon;
import carbon.view.CornersView;

public enum ShadowShape {
    RECT, ROUND_RECT, CIRCLE, CONVEX_PATH;

    public static ViewOutlineProvider viewOutlineProvider;

    static {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            viewOutlineProvider = new ViewOutlineProvider() {
                @SuppressLint("NewApi")
                @Override
                public void getOutline(View view, Outline outline) {
                    ShadowShape shadowShape = ((ShadowView) view).getShadowShape();
                    if (shadowShape == RECT) {
                        outline.setRect(0, 0, view.getWidth(), view.getHeight());
                    } else if (shadowShape == ROUND_RECT) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), ((CornersView) view).getCorners().getTopStart());
                    } else if (shadowShape == CIRCLE) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    }else{
                        outline.setConvexPath(((CornersView) view).getCorners().getPath(view.getWidth(), view.getHeight()));
                    }
                }
            };
        }
    }
}
