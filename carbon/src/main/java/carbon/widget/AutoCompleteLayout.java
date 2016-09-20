package carbon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import carbon.Carbon;
import carbon.R;
import carbon.recycler.RowListAdapter;

/**
 * Created by Marcin on 2016-09-19.
 */

public class AutoCompleteLayout extends LinearLayout {
    AutoCompleteTextView search;
    RecyclerView results;

    public AutoCompleteLayout(Context context) {
        super(context);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, R.attr.carbon_autoCompleteLayoutStyle);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, defStyleAttr);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initAutoCompleteLayout();
    }

    private void initAutoCompleteLayout() {
        View.inflate(getContext(), R.layout.carbon_autocompletelayout, this);
        setOrientation(VERTICAL);
        search = (AutoCompleteTextView) findViewById(R.id.carbon_autoCompleteSearch);
        results = (RecyclerView) findViewById(R.id.carbon_autoCompleteResults);
        results.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ColorDrawable colorDrawable = new ColorDrawable(Carbon.getThemeColor(getContext(), R.attr.carbon_dividerColor));
        int dividerWidth = getResources().getDimensionPixelSize(R.dimen.carbon_1dip);
        results.addItemDecoration(new AutoCompleteLayout.DividerItemDecoration(colorDrawable, dividerWidth));
        RowListAdapter<AutoCompleteTextView.FilteringResult> adapter = search.getAdapter();
        results.setAdapter(adapter);
        adapter.setOnItemClickedListener(position -> search.performCompletion(adapter.getItems().get(position).text.toString()));
    }

    public void setDataProvider(AutoCompleteTextView.AutoCompleteDataProvider dataProvider) {
        search.setDataProvider(dataProvider);
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {
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

            if (childCount >= 1) {
                View child = parent.getChildAt(0);
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
}
