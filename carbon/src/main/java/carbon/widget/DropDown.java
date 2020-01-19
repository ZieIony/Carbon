package carbon.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.ButtonGravity;
import carbon.drawable.ripple.RippleDrawable;
import carbon.internal.DropDownMenu;
import carbon.recycler.ListAdapter;
import carbon.recycler.RowListAdapter;

public class DropDown extends EditText {

    private CustomItemFactory customItemFactory = text -> text;

    public enum PopupMode {
        Over, Fit;
    }

    public enum Mode {
        SingleSelect, MultiSelect, Editable;
    }

    public interface OnItemSelectedListener<Type> {
        void onItemSelected(Type item, int position);
    }

    public interface OnSelectionChangedListener<Type> {
        void onSelectionChanged(Type item, int position);
    }

    private List items = new ArrayList<>();

    DropDownMenu dropDownMenu;

    OnItemSelectedListener onItemSelectedListener;
    OnSelectionChangedListener onSelectionChangedListener;

    private boolean isShowingPopup = false;

    private Drawable drawable;
    private float drawablePadding;
    private ButtonGravity buttonGravity;

    public DropDown(Context context) {
        super(context, null, R.attr.carbon_dropDownStyle);
        initDropDown(context, null, R.attr.carbon_dropDownStyle);
    }

    public DropDown(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_dropDownStyle);
        initDropDown(context, attrs, R.attr.carbon_dropDownStyle);
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDropDown(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DropDown(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDropDown(context, attrs, defStyleAttr);
    }

    private void initDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DropDown, defStyleAttr, R.style.carbon_DropDown);

        int theme = a.getResourceId(R.styleable.DropDown_carbon_popupTheme, -1);

        dropDownMenu = new DropDownMenu(new ContextThemeWrapper(context, theme));
        dropDownMenu.setOnDismissListener(() -> isShowingPopup = false);
        dropDownMenu.setPopupMode(PopupMode.values()[a.getInt(R.styleable.DropDown_carbon_popupMode, PopupMode.Over.ordinal())]);
        setMode(Mode.values()[a.getInt(R.styleable.DropDown_carbon_mode, Mode.SingleSelect.ordinal())]);
        dropDownMenu.setOnItemClickedListener(onItemClickedListener);

        setButtonDrawable(Carbon.getDrawable(this, a, R.styleable.DropDown_android_button, R.drawable.carbon_dropdown));

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.DropDown_android_drawablePadding) {
                drawablePadding = a.getDimension(attr, 0);
            } else if (attr == R.styleable.DropDown_carbon_buttonGravity) {
                buttonGravity = ButtonGravity.values()[a.getInt(attr, 0)];
            }
        }

        a.recycle();
    }

    public PopupMode getPopupMode() {
        return dropDownMenu.getPopupMode();
    }

    public void setPopupMode(PopupMode popupMode) {
        dropDownMenu.setPopupMode(popupMode);
    }

    public Mode getStyle() {
        return dropDownMenu.getMode();
    }

    public void setMode(@NonNull Mode mode) {
        dropDownMenu.setMode(mode);
        if (mode == Mode.Editable) {
            setFocusableInTouchMode(true);
            setCursorVisible(true);
            setLongClickable(true);
        } else {
            setFocusableInTouchMode(false);
            setCursorVisible(false);
            setLongClickable(false);
        }
    }

    public void setSelectedIndex(int index) {
        dropDownMenu.setSelectedIndex(index);
        setText(getAdapter().getItem(index).toString());
    }

    public void setSelectedIndices(int[] indices) {
        dropDownMenu.setSelectedIndices(indices);
    }

    public int getSelectedIndex() {
        return dropDownMenu.getSelectedIndex();
    }

    public int[] getSelectedIndices() {
        return dropDownMenu.getSelectedIndices();
    }

    public <Type extends Serializable> void setSelectedItem(Type item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                setSelectedIndex(i);
                return;
            }
        }
    }

    public <Type extends Serializable> void setSelectedItems(List<Type> items) {
        dropDownMenu.setSelectedItems(items);
    }

    public <Type extends Serializable> Type getSelectedItem() {
        return dropDownMenu.getSelectedItem();
    }

    public <Type extends Serializable> List<Type> getSelectedItems() {
        return dropDownMenu.getSelectedItems();
    }

    public void setAdapter(final RowListAdapter<Serializable> adapter) {
        dropDownMenu.setAdapter(adapter);
        setText(dropDownMenu.getSelectedText());
    }

    public ListAdapter<?, Serializable> getAdapter() {
        return dropDownMenu.getAdapter();
    }

    GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showMenu();
            return true;
        }
    });

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dropDownMenu.getMode() != Mode.Editable ||
                (isButtonOnTheLeft() && event.getX() <= getCompoundPaddingLeft() ||
                        !isButtonOnTheLeft() && event.getX() >= getWidth() - getCompoundPaddingRight())) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void showMenu() {
        dropDownMenu.setCustomItem(customItemFactory.makeItem(getText().toString()));
        dropDownMenu.show(DropDown.this);
        isShowingPopup = true;
    }

    public interface CustomItemFactory {
        Serializable makeItem(String text);
    }

    public void setCustomItemFactory(CustomItemFactory factory) {
        customItemFactory = factory;
    }

    RecyclerView.OnItemClickedListener<Serializable> onItemClickedListener = new RecyclerView.OnItemClickedListener<Serializable>() {
        @Override
        public void onItemClicked(View view, Serializable item, int position) {
            Mode mode = dropDownMenu.getMode();
            if (mode == Mode.MultiSelect) {
                dropDownMenu.toggle(position);
                if (onItemSelectedListener != null)
                    onItemSelectedListener.onItemSelected(item, position);
                if (onSelectionChangedListener != null)
                    onSelectionChangedListener.onSelectionChanged(item, position);
            } else {
                int prevSelectedIndex = getSelectedIndex();
                setSelectedIndex(position);
                if (onItemSelectedListener != null)
                    onItemSelectedListener.onItemSelected(item, position);
                if (onSelectionChangedListener != null && prevSelectedIndex != position)
                    onSelectionChangedListener.onSelectionChanged(item, position);
            }

            setText(dropDownMenu.getSelectedText());
            if (mode != Mode.MultiSelect)
                dropDownMenu.dismiss();
        }
    };

    public <Type extends Serializable> void setOnItemSelectedListener(OnItemSelectedListener<Type> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public <Type extends Serializable> void setOnSelectionChangedListener(OnSelectionChangedListener<Type> onSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener;
    }

    public <Type extends Serializable> void setItems(Type[] items) {
        this.items.clear();
        this.items.addAll(Arrays.asList(items));
        dropDownMenu.setItems(this.items);
        setSelectedIndex(0);
    }

    public <Type extends Serializable> void setItems(List<Type> items) {
        this.items = items;
        dropDownMenu.setItems(items);
        setSelectedIndex(0);
    }

    public static class Adapter<Type> extends ListAdapter<ViewHolder, Type> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_dropdown_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv.setText(items.get(position).toString());
            holder.itemView.setOnClickListener(view -> fireOnItemClickedEvent(holder.itemView, holder.getAdapterPosition()));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.carbon_itemText);
        }
    }

    public static class CheckableAdapter<Type> extends ListAdapter<CheckableViewHolder, Type> {
        private List<Integer> selectedIndices;

        public CheckableAdapter(List<Integer> selectedIndices) {
            this.selectedIndices = selectedIndices;
        }

        @Override
        public CheckableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_popupmenu_checkableitem, parent, false);
            return new CheckableViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CheckableViewHolder holder, final int position) {
            holder.checkBox.setText(items.get(position).toString());
            holder.checkBox.setChecked(selectedIndices.contains(position));
            holder.itemView.setOnClickListener(view -> fireOnItemClickedEvent(holder.itemView, holder.getAdapterPosition()));
        }
    }

    public static class CheckableViewHolder extends RecyclerView.ViewHolder implements Checkable {

        CheckBox checkBox;

        public CheckableViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.carbon_itemCheckText);
        }

        @Override
        public void setChecked(boolean b) {
            checkBox.setChecked(b);
        }

        @Override
        public boolean isChecked() {
            return checkBox.isChecked();
        }

        @Override
        public void toggle() {
            checkBox.toggle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed && dropDownMenu != null) {
            carbon.widget.FrameLayout container = dropDownMenu.getContentView().findViewById(R.id.carbon_popupContainer);
            if (container.getAnimator() == null)    // TODO: check this check
                dropDownMenu.update();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isShowingPopup)
            dropDownMenu.showImmediate(DropDown.this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isShowingPopup)
            dropDownMenu.dismissImmediate();
    }

    private boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private boolean isButtonOnTheLeft() {
        return buttonGravity == ButtonGravity.LEFT ||
                !isLayoutRtl() && buttonGravity == ButtonGravity.START ||
                isLayoutRtl() && buttonGravity == ButtonGravity.END;
    }

    /**
     * Set the button graphic to a given Drawable
     *
     * @param d The Drawable to use as the button graphic
     */
    public void setButtonDrawable(Drawable d) {
        if (drawable != d) {
            if (drawable != null) {
                drawable.setCallback(null);
                unscheduleDrawable(drawable);
            }

            drawable = d;

            if (d != null) {
                drawable = DrawableCompat.wrap(d);
                d.setCallback(this);
                //d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                d.setVisible(getVisibility() == VISIBLE, false);
                setMinHeight(d.getIntrinsicHeight());
                updateButtonTint();
            }
        }
    }

    public ButtonGravity getButtonGravity() {
        return buttonGravity;
    }

    public void setButtonGravity(ButtonGravity buttonGravity) {
        this.buttonGravity = buttonGravity;
    }

    @Override
    public void setTintList(ColorStateList list) {
        super.setTintList(list);
        updateButtonTint();
    }

    @Deprecated
    public void setTint(@Nullable ColorStateList list) {
        super.setTintList(list);
        updateButtonTint();
    }

    @Override
    public void setTint(int color) {
        setTintList(ColorStateList.valueOf(color));
    }

    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        super.setTintMode(mode);
        updateButtonTint();
    }

    private void updateButtonTint() {
        if (drawable != null) {
            if (tint != null && tintMode != null) {
                Carbon.setTintListMode(drawable, tint, tintMode);
            } else {
                Carbon.clearTint(drawable);
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful())
                drawable.setState(getDrawableState());
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(DropDown.class.getName());
    }

    /*@Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(DropDown.class.getName());
        info.setCheckable(true);
        info.setChecked(mChecked);
    }*/

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (isButtonOnTheLeft()) {
            final Drawable buttonDrawable = drawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth() + drawablePadding;
            }
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (!isButtonOnTheLeft()) {
            final Drawable buttonDrawable = drawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth() + drawablePadding;
            }
        }
        return padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Drawable buttonDrawable = drawable;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();

            final int top;
            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    top = Math.max(getPaddingTop(), getHeight() - drawableHeight - getPaddingBottom());
                    break;
                case Gravity.CENTER_VERTICAL:
                    top = Math.max(getPaddingTop(), (getHeight() - drawableHeight) / 2);
                    break;
                default:
                    top = getPaddingTop();
            }
            final int bottom = top + drawableHeight;
            final int left = isButtonOnTheLeft() ? getPaddingLeft() : getWidth() - drawableWidth - getPaddingRight();
            final int right = isButtonOnTheLeft() ? drawableWidth + getPaddingLeft() : getWidth() - getPaddingRight();

            buttonDrawable.setBounds(left, top, right, bottom);

            final Drawable background = getBackground();
            if (background instanceof RippleDrawable) {
                //TODO: hotspotBounds
                // ((RippleDrawable)background).setHotspotBounds(left, top, right, bottom);
            }
        }

        super.onDraw(canvas);

        if (buttonDrawable != null) {
            // TODO: get rid of invalidate() loop
            if (animateColorChanges && tint != null && tintMode != null)
                buttonDrawable.setColorFilter(new PorterDuffColorFilter(tint.getColorForState(buttonDrawable.getState(), tint.getDefaultColor()), tintMode));

            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            if (scrollX == 0 && scrollY == 0) {
                buttonDrawable.draw(canvas);
            } else {
                canvas.translate(scrollX, scrollY);
                buttonDrawable.draw(canvas);
                canvas.translate(-scrollX, -scrollY);
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Drawable d = drawable;
        if (d != null && d.isStateful()
                && d.setState(getDrawableState())) {
            invalidateDrawable(d);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == drawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (drawable != null) drawable.jumpToCurrentState();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.isShowingPopup = this.isShowingPopup ? 1 : 0;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.isShowingPopup = ss.isShowingPopup > 0;
    }

    static class SavedState implements Parcelable {

        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        int isShowingPopup;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(EditText.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.isShowingPopup = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeInt(this.isShowingPopup);
        }

        public Parcelable getSuperState() {
            return superState;
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
