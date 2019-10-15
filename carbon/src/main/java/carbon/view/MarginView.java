package carbon.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

public interface MarginView {
    default void setMargins(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(layoutParams);
    }

    default void setMargins(int left, int top, int right, int bottom) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(layoutParams);
    }

    default void setMarginStart(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        int layoutDirection = ViewCompat.getLayoutDirection(view);
        if (layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, margin, layoutParams.bottomMargin);
        } else {
            layoutParams.setMargins(margin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        }
        view.setLayoutParams(layoutParams);
    }

    default void setMarginEnd(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        int layoutDirection = ViewCompat.getLayoutDirection(view);
        if (layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, margin, layoutParams.bottomMargin);
        } else {
            layoutParams.setMargins(margin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        }
        view.setLayoutParams(layoutParams);
    }

    default void setMarginLeft(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(margin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    default void setMarginRight(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, margin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    default void setMarginTop(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(layoutParams.leftMargin, margin, layoutParams.rightMargin, layoutParams.bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    default void setMarginBottom(int margin) {
        View view = (View) this;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams))
            throw new IllegalStateException("Invalid layoutParams. Unable to set margin.");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, margin);
        view.setLayoutParams(layoutParams);
    }
}
