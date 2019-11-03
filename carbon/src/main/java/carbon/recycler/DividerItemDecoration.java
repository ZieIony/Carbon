package carbon.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import carbon.R;
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.ColorStateListFactory;
import carbon.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public interface DrawRules {
        boolean draw(int position);
    }

    private Drawable drawable;
    private int height;
    private DrawRules drawBeforeRules, drawAfterRules;

    public DividerItemDecoration(Context context) {
        drawable = new ColorStateListDrawable(ColorStateListFactory.INSTANCE.makeControl(context));
        height = context.getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight);
    }

    public DividerItemDecoration(Drawable drawable, int height) {
        this.drawable = drawable;
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, androidx.recyclerview.widget.RecyclerView parent, androidx.recyclerview.widget.RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (drawable == null)
            return;
        int position = parent.getChildAdapterPosition(view);
        if (position == -1)
            return;

        if (drawAfterRules != null && drawAfterRules.draw(position)) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.bottom = height;
            } else {
                outRect.right = height;
            }
        }
        if (drawBeforeRules != null && drawBeforeRules.draw(position) || drawAfterRules == null && drawBeforeRules == null && position > 0) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = height;
            } else {
                outRect.left = height;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, androidx.recyclerview.widget.RecyclerView parent, androidx.recyclerview.widget.RecyclerView.State state) {
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

        for (int i = 0; i < childCount; i++) {
            int position = parent.getChildAdapterPosition(parent.getChildAt(i));
            if (position == -1)
                continue;
            if (drawAfterRules != null && drawAfterRules.draw(position)) {
                View child = parent.getChildAt(i);

                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = (int) (child.getBottom() + child.getTranslationY());
                    bottom = top + height;
                } else { //horizontal
                    left = (int) (child.getRight() + child.getTranslationX());
                    right = left + height;
                }
                c.save();
                c.clipRect(left, top, right, bottom);
                drawable.setAlpha((int) (child.getAlpha() * 255));
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
                c.restore();
            }
            if (drawBeforeRules != null && drawBeforeRules.draw(position) || drawAfterRules == null && drawBeforeRules == null && position > 0) {
                View child = parent.getChildAt(i);

                if (orientation == LinearLayoutManager.VERTICAL) {
                    bottom = (int) (child.getTop() + child.getTranslationY());
                    top = bottom - height;
                } else { //horizontal
                    right = (int) (child.getLeft() + child.getTranslationX());
                    left = right - height;
                }
                c.save();
                c.clipRect(left, top, right, bottom);
                drawable.setAlpha((int) (child.getAlpha() * 255));
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
                c.restore();
            }
        }
    }

    private int getOrientation(androidx.recyclerview.widget.RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else {
            throw new IllegalStateException(
                    "DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
    }

    public void setDrawAfter(DrawRules drawRules) {
        this.drawAfterRules = drawRules;
    }

    public void setDrawBefore(DrawRules drawRules) {
        this.drawBeforeRules = drawRules;
    }

}