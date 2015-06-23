package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import carbon.R;

/**
 * Created by Marcin on 2015-06-11.
 */
public class Spinner extends EditText {
    PopupMenu popupMenu;
    private boolean isShowingPopup = false;

    public Spinner(Context context) {
        this(context, null);
    }

    public Spinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_spinnerStyle);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        popupMenu = new PopupMenu(context);
        defaultAdapter = new Adapter();
        popupMenu.setAdapter(defaultAdapter);
        popupMenu.setTint(getTint());
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowingPopup = false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show(Spinner.this);
                isShowingPopup = true;
            }
        });
    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        if (adapter == null) {
            popupMenu.setAdapter(defaultAdapter);
            defaultAdapter.setOnItemClickedListener(onItemClickedListener);
        } else {
            popupMenu.setAdapter(adapter);
            adapter.setOnItemClickedListener(onItemClickedListener);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return popupMenu.getAdapter();
    }

    RecyclerView.OnItemClickedListener onItemClickedListener = new RecyclerView.OnItemClickedListener() {
        @Override
        public void onItemClicked(int position) {
            setText(popupMenu.getAdapter().getItem(position).toString());
            popupMenu.dismiss();
        }
    };

    Adapter defaultAdapter;

    public void setItems(String[] items) {
        popupMenu.setAdapter(defaultAdapter);
        defaultAdapter.setOnItemClickedListener(onItemClickedListener);
        defaultAdapter.setItems(items);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder, String> {

        private String[] items = new String[0];

        @Override
        public String getItem(int position) {
            return items[position];
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_popup_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            super.onBindViewHolder(holder, position);
            holder.tv.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }


        public void setItems(String[] items) {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.carbon_itemText);
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);

        if (popupMenu != null)
            popupMenu.update();

        return result;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isShowingPopup)
            popupMenu.showImmediate(Spinner.this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isShowingPopup)
            popupMenu.dismissImmediate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.isShowingPopup ? 1 : 0;

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

        this.isShowingPopup = ss.stateToSave > 0;
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
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


    // -------------------------------
    // tint
    // -------------------------------

    @Override
    public void setTint(ColorStateList list) {
        super.setTint(list);
        if (popupMenu != null)
            popupMenu.setTint(list);
    }
}
