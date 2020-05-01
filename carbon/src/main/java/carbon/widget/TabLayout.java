package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.annimon.stream.Stream;

import carbon.R;
import carbon.component.Component;
import carbon.component.LayoutComponent;
import carbon.databinding.CarbonTablayoutTabBinding;
import carbon.recycler.RowFactory;

public class TabLayout extends HorizontalScrollView {

    public static class Item {
        private CharSequence title;

        public Item(CharSequence title) {
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    private static class ItemComponent extends LayoutComponent<Item> {
        ItemComponent(ViewGroup parent) {
            super(parent, R.layout.carbon_tablayout_tab);
        }

        private CarbonTablayoutTabBinding binding = CarbonTablayoutTabBinding.bind(getView());

        @Override
        public void bind(Item data) {
            binding.carbonTabText.setText(data.title);
        }
    }

    ViewPager viewPager;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    LinearLayout content;
    private float indicatorPos = 0;
    private int selectedPage = 0;
    private float indicatorPos2 = 0;
    float indicatorHeight;
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    boolean fixed = false;
    Item[] items;
    private RowFactory<Item> itemFactory;

    private ValueAnimator animator, animator2;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            position = Math.round(position + positionOffset);
            if (position != selectedPage) {
                View view = content.getChildAt(position);
                if (view == null)
                    return; // TODO: what's really going on here? #130

                if (animator != null)
                    animator.cancel();
                if (animator2 != null)
                    animator2.cancel();

                animator = ValueAnimator.ofFloat(indicatorPos, view.getLeft());
                animator.setDuration(200);
                if (position > selectedPage)
                    animator.setStartDelay(100);
                animator.setInterpolator(decelerateInterpolator);
                animator.addUpdateListener(animation -> {
                    indicatorPos = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                animator.start();

                animator2 = ValueAnimator.ofFloat(indicatorPos2, view.getRight());
                animator2.setDuration(200);
                if (position < selectedPage)
                    animator2.setStartDelay(100);
                animator2.setInterpolator(decelerateInterpolator);
                animator2.addUpdateListener(animation -> {
                    indicatorPos2 = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                animator2.start();

                setSelectedPage(position);

                if (content.getChildAt(selectedPage).getLeft() - getScrollX() < 0) {
                    smoothScrollTo(content.getChildAt(selectedPage).getLeft(), 0);
                } else if (content.getChildAt(selectedPage).getRight() - getScrollX() > getWidth()) {
                    smoothScrollTo(content.getChildAt(selectedPage).getRight() - getWidth() + getPaddingLeft(), 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public TabLayout(Context context) {
        super(context, null, R.attr.carbon_tabLayoutStyle);
        initPagerTabStrip(null, R.attr.carbon_tabLayoutStyle, R.style.carbon_TabLayout);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_tabLayoutStyle);
        initPagerTabStrip(attrs, R.attr.carbon_tabLayoutStyle, R.style.carbon_TabLayout);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPagerTabStrip(attrs, defStyleAttr, R.style.carbon_TabLayout);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPagerTabStrip(attrs, defStyleAttr, defStyleRes);
    }

    private void initPagerTabStrip(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);
        content = new LinearLayout(getContext());
        ViewCompat.setLayoutDirection(content, layoutDirection);
        addView(content, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyleAttr, defStyleRes);

        setIndicatorHeight(a.getDimension(R.styleable.TabLayout_carbon_indicatorWidth, 2));
        setFixed(a.getBoolean(R.styleable.TabLayout_carbon_fixedTabs, true));
        itemFactory = ItemComponent::new;

        a.recycle();

        setHorizontalFadingEdgeEnabled(false);
        setHorizontalScrollBarEnabled(false);

        initTabs();
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);
        this.viewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(pageChangeListener);
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null && items == null)
                items = Stream.range(0, adapter.getCount()).map(it -> new Item(adapter.getPageTitle(it))).toArray(Item[]::new);
        }
        initTabs();
    }

    @Deprecated
    public void setItemLayout(int itemLayoutId) {
    }

    public void setItemFactory(RowFactory<Item> factory) {
        this.itemFactory = factory;
        initTabs();
    }

    public void setItems(Item[] items) {
        this.items = items;
        initTabs();
    }

    private void initTabs() {
        content.removeAllViews();

        if (items == null)
            return;

        for (int i = 0; i < items.length; i++) {
            Component<Item> component = itemFactory.create(this);
            component.bind(items[i]);
            content.addView(component.getView(), new LinearLayout.LayoutParams(fixed ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            component.getView().setSelected(i == 0);
            final int finalI = i;
            component.getView().setOnClickListener(__ -> {
                if (viewPager != null) {
                    viewPager.setCurrentItem(finalI);
                } else {
                    pageChangeListener.onPageScrolled(finalI, 0, 0);
                }
            });
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (content.getChildCount() == 0)
            return;
        if (indicatorPos == indicatorPos2)
            indicatorPos2 = content.getChildAt(selectedPage).getWidth();
        paint.setColor(getTint().getColorForState(getDrawableState(), getTint().getDefaultColor()));
        canvas.drawRect(indicatorPos + getPaddingLeft(), getHeight() - indicatorHeight - getPaddingBottom(), indicatorPos2 + getPaddingLeft(), getHeight(), paint);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
        setFillViewport(fixed);
        initTabs();
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        postInvalidate();
    }

    public void setSelectedPage(int position) {
        if (content.getChildCount() > selectedPage)
            content.getChildAt(selectedPage).setSelected(false);
        selectedPage = position;
        if (content.getChildCount() > selectedPage)
            content.getChildAt(selectedPage).setSelected(true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, selectedPage, getScrollX(), indicatorPos, indicatorPos2);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setSelectedPage(savedState.getSelectedPage());
        indicatorPos = savedState.getIndicatorPos();
        indicatorPos2 = savedState.getIndicatorPos2();
        post(new Runnable() {
            public void run() {
                setScrollX(savedState.getScroll());
            }
        });
    }

    @Override
    protected void dispatchSaveInstanceState(@NonNull SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(@NonNull SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    protected static class SavedState extends BaseSavedState {
        private final int selectedPage;
        private final int scroll;
        private final float indicatorPos;
        private final float indicatorPos2;

        private SavedState(Parcelable superState, int selectedPage, int scrollX, float indicatorPos, float indicatorPos2) {
            super(superState);
            this.selectedPage = selectedPage;
            this.scroll = scrollX;
            this.indicatorPos = indicatorPos;
            this.indicatorPos2 = indicatorPos2;
        }

        private SavedState(Parcel in) {
            super(in);
            selectedPage = in.readInt();
            scroll = in.readInt();
            indicatorPos = in.readFloat();
            indicatorPos2 = in.readFloat();
        }

        public int getSelectedPage() {
            return selectedPage;
        }

        public int getScroll() {
            return scroll;
        }

        public float getIndicatorPos() {
            return indicatorPos;
        }

        public float getIndicatorPos2() {
            return indicatorPos2;
        }

        @Override
        public void writeToParcel(@NonNull Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(selectedPage);
            destination.writeInt(scroll);
            destination.writeFloat(indicatorPos);
            destination.writeFloat(indicatorPos2);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
