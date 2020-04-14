package carbon.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import carbon.widget.RecyclerView;

public class ViewItemDecoration extends RecyclerView.ItemDecoration {

    public interface DrawRules {
        boolean draw(int position);
    }

    private View view;
    private DrawRules drawBeforeRules, drawAfterRules;

    public ViewItemDecoration(Context context, int viewId) {
        view = LayoutInflater.from(context).inflate(viewId, null);
    }

    public ViewItemDecoration(View view) {
        this.view = view;
    }

    public void measure(RecyclerView parent) {
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            view.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        }
    }

    public void layout(RecyclerView parent) {
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            view.layout(parent.getPaddingLeft(),
                    0,
                    parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(),
                    view.getMeasuredHeight());
        } else {
            view.layout(0,
                    parent.getPaddingTop(),
                    view.getMeasuredWidth(),
                    parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom());
        }
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View child, @NotNull androidx.recyclerview.widget.RecyclerView parent, @NotNull androidx.recyclerview.widget.RecyclerView.State state) {
        super.getItemOffsets(outRect, child, parent, state);
        int position = parent.getChildAdapterPosition(child);
        if (position == -1)
            return;

        if (drawAfterRules != null && drawAfterRules.draw(position)) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.bottom = view.getMeasuredHeight();
            } else {
                outRect.right = view.getMeasuredWidth();
            }
        }
        if (drawBeforeRules != null && drawBeforeRules.draw(position) || drawAfterRules == null && drawBeforeRules == null && position > 0) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = view.getMeasuredHeight();
            } else {
                outRect.left = view.getMeasuredWidth();
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView parent, @NonNull androidx.recyclerview.widget.RecyclerView.State state) {
        // Initialization needed to avoid compiler warning
        int left = 0;
        int top = 0;
        int orientation = getOrientation(parent);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            int position = parent.getChildAdapterPosition(parent.getChildAt(i));
            if (position == -1)
                continue;
            if (drawAfterRules != null && drawAfterRules.draw(position)) {
                View child = parent.getChildAt(i);

                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = (int) (child.getBottom() + child.getTranslationY());
                } else {    // horizontal
                    left = (int) (child.getRight() + child.getTranslationX());
                }
                view.setAlpha(child.getAlpha());
                c.translate(left, top);
                view.draw(c);
                c.translate(-left, -top);
            }
            if (drawBeforeRules != null && drawBeforeRules.draw(position) || drawAfterRules == null && drawBeforeRules == null && position > 0) {
                View child = parent.getChildAt(i);

                if (orientation == LinearLayoutManager.VERTICAL) {
                    top = (int) (child.getTop() + child.getTranslationY() - view.getMeasuredHeight());
                } else {    // horizontal
                    left = (int) (child.getLeft() + child.getTranslationX() - view.getMeasuredWidth());
                }
                view.setAlpha(child.getAlpha());
                c.translate(left, top);
                view.draw(c);
                c.translate(-left, -top);
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