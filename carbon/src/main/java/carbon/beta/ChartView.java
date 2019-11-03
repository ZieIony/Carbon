package carbon.beta;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Arrays;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.view.View;

public class ChartView extends View {

    public enum ChartType {
        Bar, Line
    }

    public static class Item {
        String name;
        float value;
        ColorStateList color;

        public Item() {
        }

        public Item(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public void setColor(ColorStateList color) {
            this.color = color;
        }

        public ColorStateList getColor() {
            return color;
        }
    }

    private Item[] items;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float spacing = 0, barCornerRadius = 0, linesThickness, chartPadding = 0, axesThickness, gridDensity, gridThickness;
    private ColorStateList itemColor, axesColor, gridColor;
    private Item selectedItem;
    private RectF rect = new RectF();
    private float maxItemHeight = 0;
    private ChartType chartType;
    private ValueAnimator.AnimatorUpdateListener colorAnimatorListener = animator -> postInvalidate();

    public ChartView(Context context) {
        super(context, null, R.attr.carbon_chartViewStyle);
        initChartView(null, R.attr.carbon_chartViewStyle, R.style.carbon_ChartView);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_chartViewStyle);
        initChartView(attrs, R.attr.carbon_chartViewStyle, R.style.carbon_ChartView);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChartView(attrs, defStyleAttr, R.style.carbon_ChartView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChartView(attrs, defStyleAttr, defStyleRes);
    }

    public void initChartView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ChartView, defStyleAttr, defStyleRes);

        /*int ap = a.getResourceId(R.styleable.Label_android_textAppearance, -1);
        if (ap != -1)
            Carbon.setTextAppearance(this, ap, a.hasValue(R.styleable.Label_android_textColor), true);*/

        setItemSpacing(a.getDimension(R.styleable.ChartView_carbon_itemSpacing, 0));
        setItemColor(Carbon.getColorStateList(this, a, R.styleable.ChartView_carbon_itemColor));
        setBarCornerRadius(a.getDimension(R.styleable.ChartView_carbon_barCornerRadius, 0));
        setLinesThickness(a.getDimension(R.styleable.ChartView_carbon_linesThickness, 1));
        setChartPadding(a.getDimension(R.styleable.ChartView_carbon_chartPadding, 0));
        setAxesColor(a.getColorStateList(R.styleable.ChartView_carbon_axesColor));
        setAxesThickness(a.getDimensionPixelSize(R.styleable.ChartView_carbon_axesThickness, 1));
        setChartType(ChartType.values()[a.getInt(R.styleable.ChartView_carbon_chartType, 0)]);
        setGridDensity(a.getDimension(R.styleable.ChartView_carbon_gridDensity, 0));
        setGridColor(a.getColorStateList(R.styleable.ChartView_carbon_gridColor));
        setGridThickness(a.getDimensionPixelSize(R.styleable.ChartView_carbon_gridThickness, 1));

        a.recycle();
    }

    public void setItems(Item[] items) {
        this.items = Arrays.copyOf(items, items.length);
        maxItemHeight = 0;
        for (Item item : items)
            maxItemHeight = Math.max(maxItemHeight, item.value);
    }

    public void setItemSpacing(float spacing) {
        this.spacing = spacing;
    }

    public void setItemColor(ColorStateList itemColor) {
        this.itemColor = isAnimateColorChangesEnabled() && !(itemColor instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(itemColor, colorAnimatorListener) : itemColor;
    }

    public void setItemColor(int itemColor) {
        setItemColor(ColorStateList.valueOf(itemColor));
    }

    public void setBarCornerRadius(float barCornerRadius) {
        this.barCornerRadius = barCornerRadius;
    }

    public float getBarCornerRadius() {
        return barCornerRadius;
    }

    public float getLinesThickness() {
        return linesThickness;
    }

    public void setLinesThickness(float linesThickness) {
        this.linesThickness = linesThickness;
    }

    public void setAxesColor(ColorStateList axesColor) {
        this.axesColor = isAnimateColorChangesEnabled() && !(axesColor instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(axesColor, colorAnimatorListener) : axesColor;
    }

    public ColorStateList getAxesColor() {
        return axesColor;
    }

    public void setChartPadding(float graphPadding) {
        this.chartPadding = graphPadding;
    }

    public float getChartPadding() {
        return chartPadding;
    }

    public void setAxesThickness(float axesThickness) {
        this.axesThickness = axesThickness;
    }

    public float getAxesThickness() {
        return axesThickness;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public float getGridDensity() {
        return gridDensity;
    }

    public void setGridDensity(float gridDensity) {
        this.gridDensity = gridDensity;
    }

    public ColorStateList getGridColor() {
        return gridColor;
    }

    public void setGridColor(ColorStateList gridColor) {
        this.gridColor = isAnimateColorChangesEnabled() && !(gridColor instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(gridColor, colorAnimatorListener) : gridColor;
    }

    public float getGridThickness() {
        return gridThickness;
    }

    public void setGridThickness(float gridThickness) {
        this.gridThickness = gridThickness;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (items == null) {
            selectedItem = null;
            return false;
        }

        float viewportWidth = getWidth() - getPaddingLeft() - getPaddingRight() - chartPadding * 2;
        float itemWidth = (viewportWidth - (items.length - 1) * spacing) / items.length;

        for (int i = 0; i < items.length; i++) {
            if (event.getX() >= chartPadding + getPaddingLeft() + i * (itemWidth + spacing) &&
                    event.getX() <= chartPadding + getPaddingLeft() + i * (itemWidth + spacing) + itemWidth) {
                selectedItem = items[i];
                postInvalidate();
                break;
            }
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (items == null || maxItemHeight == 0)
            return;

        float viewportWidth = getWidth() - getPaddingLeft() - getPaddingRight() - chartPadding * 2;
        float viewportHeight = getHeight() - getPaddingTop() - getPaddingBottom() - chartPadding;

        paint.setStrokeWidth(gridThickness);
        paint.setColor(gridColor.getColorForState(getDrawableState(), gridColor.getDefaultColor()));
        for (float y = viewportHeight + chartPadding - gridDensity; y >= chartPadding; y -= gridDensity)
            canvas.drawLine(getPaddingLeft(), y + getPaddingTop(), getWidth() - getPaddingRight(), y + getPaddingTop(), paint);

        paint.setStrokeWidth(axesThickness);
        paint.setColor(axesColor.getColorForState(getDrawableState(), axesColor.getDefaultColor()));
        canvas.drawLine(getPaddingLeft(), getPaddingTop(), getPaddingLeft(), getHeight() - getPaddingBottom(), paint);
        canvas.drawLine(getPaddingLeft(), getHeight() - getPaddingBottom(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), paint);

        paint.setStrokeWidth(linesThickness);
        int saveCount = canvas.save();
        canvas.clipRect(getPaddingLeft() + chartPadding, getPaddingTop(), getWidth() - getPaddingRight() - chartPadding, getHeight() - getPaddingBottom());
        canvas.translate(getPaddingLeft() + chartPadding, getPaddingTop() + chartPadding);
        switch (chartType) {
            case Bar:
                drawBarChart(canvas, viewportWidth, viewportHeight);
                break;
            case Line:
                drawLineChart(canvas, viewportWidth, viewportHeight);
                break;
        }
        canvas.restoreToCount(saveCount);
    }

    private void drawBarChart(Canvas canvas, float width, float height) {
        float itemWidth = (width - (items.length - 1) * spacing) / items.length;

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            ColorStateList color = item.color != null ? item.color : itemColor;
            if (itemColor == null)
                continue;
            paint.setColor(color.getColorForState(getDrawableStateSelected(selectedItem == item), color.getDefaultColor()));
            rect.set(i * (itemWidth + spacing),
                    height - height / maxItemHeight * item.value,
                    i * (itemWidth + spacing) + itemWidth,
                    height + barCornerRadius);
            canvas.drawRoundRect(rect, barCornerRadius, barCornerRadius, paint);
        }
    }

    private void drawLineChart(Canvas canvas, float width, float height) {
        float itemWidth = (width - (items.length - 1) * spacing) / items.length;

        for (int i = 0; i < items.length - 1; i++) {
            Item item = items[i];
            Item item2 = items[i + 1];
            ColorStateList color = item.color != null ? item.color : itemColor;
            if (itemColor == null)
                continue;
            paint.setColor(color.getColorForState(getDrawableStateSelected(selectedItem == item), color.getDefaultColor()));
            canvas.drawLine(i * (itemWidth + spacing) + itemWidth / 2,
                    height - height / maxItemHeight * item.value,
                    (i + 1) * (itemWidth + spacing) + itemWidth / 2,
                    height - height / maxItemHeight * item2.value, paint);
        }
    }

    private int[] getDrawableStateSelected(boolean selected) {
        if (!selected) {
            return getDrawableState();
        } else {
            int[] newState = Arrays.copyOf(getDrawableState(), getDrawableState().length + 1);
            newState[newState.length - 1] = android.R.attr.state_selected;
            return newState;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (axesColor instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) axesColor).setState(getDrawableState());
        if (itemColor instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) itemColor).setState(getDrawableState());
        if (gridColor instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) gridColor).setState(getDrawableState());
    }
}
