package carbon.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import carbon.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable drawable;
    private int height;

    public DividerItemDecoration(Drawable drawable, int height) {
        this.drawable = drawable;
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (drawable == null)
            return;
        if (parent.getChildPosition(view) < 1)
            return;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = height;
        } else {
            outRect.left = height;
        }
    }

    @Override
    public void onDrawOver(Canvas c, android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.State state) {
        if (drawable == null) {
            super.onDrawOver(c, parent, state);
            return;
        }

        // Initialization needed to avoid compiler warning
        int left = 0, right = 0, top = 0, bottom = 0;
        int orientation = getOrientation(parent);
        int childCount = parent.getChildCount();

        if (orientation == LinearLayoutManager.VERTICAL) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else { //horizontal
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = 1; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (orientation == LinearLayoutManager.VERTICAL) {
                bottom = (int) (child.getTop() - params.topMargin + ViewHelper.getTranslationY(child));
                top = bottom - height;
            } else { //horizontal
                right = (int) (child.getLeft() - params.leftMargin + ViewHelper.getTranslationX(child));
                left = right - height;
            }
            c.save(Canvas.CLIP_SAVE_FLAG);
            c.clipRect(left, top, right, bottom);
            drawable.setAlpha((int) (ViewHelper.getAlpha(child) * 255));
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
            c.restore();
        }
    }

    private int getOrientation(android.support.v7.widget.RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else {
            throw new IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
    }
}