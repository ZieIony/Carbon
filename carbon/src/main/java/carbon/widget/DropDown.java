package carbon.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.VectorDrawable;
import carbon.internal.DropDownMenu;
import carbon.recycler.ListAdapter;
import carbon.recycler.RowListAdapter;

public class DropDown<Type extends Serializable> extends EditText {

    private VectorDrawable arrowDrawable;
    private CustomItemFactory<Type> customItemFactory = text -> (Type) text;

    public enum Mode {
        Over, Fit;
    }

    public enum Style {
        SingleSelect, MultiSelect, Editable;
    }

    public interface OnItemSelectedListener<Type> {
        void onItemSelected(Type item, int position);
    }

    public interface OnSelectionChangedListener<Type> {
        void onSelectionChanged(Type item, int position);
    }

    private List<Type> items = new ArrayList<>();

    DropDownMenu<Type> dropDownMenu;

    OnItemSelectedListener<Type> onItemSelectedListener;
    OnSelectionChangedListener<Type> onSelectionChangedListener;

    private boolean isShowingPopup = false;

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
        if(!isInEditMode()) {
            arrowDrawable = new VectorDrawable(getResources(), R.raw.carbon_dropdown);
            int size = (int) (Carbon.getDip(getContext()) * 24);
            arrowDrawable.setBounds(0, 0, size, size);
            setCompoundDrawables(null, null, arrowDrawable, null);
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DropDown, defStyleAttr, R.style.carbon_DropDown);

        int theme = a.getResourceId(R.styleable.DropDown_carbon_popupTheme, -1);

        dropDownMenu = new DropDownMenu<Type>(new ContextThemeWrapper(context, theme));
        dropDownMenu.setOnDismissListener(() -> isShowingPopup = false);
        dropDownMenu.setMode(Mode.values()[a.getInt(R.styleable.DropDown_carbon_mode, Mode.Over.ordinal())]);
        setStyle(Style.values()[a.getInt(R.styleable.DropDown_carbon_style, Style.SingleSelect.ordinal())]);
        dropDownMenu.setOnItemClickedListener(onItemClickedListener);

        a.recycle();
    }

    public Mode getMode() {
        return dropDownMenu.getMode();
    }

    public void setMode(Mode mode) {
        dropDownMenu.setMode(mode);
    }

    public Style getStyle() {
        return dropDownMenu.getStyle();
    }

    public void setStyle(@NonNull Style style) {
        dropDownMenu.setStyle(style);
        if (style == DropDown.Style.Editable) {
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

    public void setSelectedItem(Type item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                setSelectedIndex(i);
                return;
            }
        }
    }

    public void setSelectedItems(List<Type> items) {
        dropDownMenu.setSelectedItems(items);
    }

    public Type getSelectedItem() {
        return dropDownMenu.getSelectedItem();
    }

    public List<Type> getSelectedItems() {
        return dropDownMenu.getSelectedItems();
    }

    public void setAdapter(final RowListAdapter<Type> adapter) {
        dropDownMenu.setAdapter(adapter);
        setText(dropDownMenu.getSelectedText());
    }

    public ListAdapter<?, Type> getAdapter() {
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
        if (dropDownMenu.getStyle() == Style.Editable &&
                event.getX() >= getWidth() - getPaddingRight() - arrowDrawable.getBounds().width() ||
                dropDownMenu.getStyle() != Style.Editable) {
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

    public interface CustomItemFactory<Type>{
        Type makeItem(String text);
    }

    public void setCustomItemFactory(CustomItemFactory<Type> factory){
        customItemFactory = factory;
    }

    RecyclerView.OnItemClickedListener<Type> onItemClickedListener = new RecyclerView.OnItemClickedListener<Type>() {
        @Override
        public void onItemClicked(View view, Type item, int position) {
            Style style = dropDownMenu.getStyle();
            if (style == Style.MultiSelect) {
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
            if (style != Style.MultiSelect)
                dropDownMenu.dismiss();
        }
    };

    public void setOnItemSelectedListener(OnItemSelectedListener<Type> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener<Type> onSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener;
    }

    public void setItems(Type[] items) {
        this.items.clear();
        this.items.addAll(Arrays.asList(items));
        dropDownMenu.setItems(this.items);
        setSelectedIndex(0);
    }

    public void setItems(List<Type> items) {
        this.items = items;
        dropDownMenu.setItems(items);
        setSelectedIndex(0);
    }

    public static class Adapter<Type> extends ListAdapter<ViewHolder, Type> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_popupmenu_item, parent, false);
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
