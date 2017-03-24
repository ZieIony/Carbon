package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.drawable.DefaultColorStateList;

/**
 * Created by Marcin on 17.03.2016.
 */
public class BottomBar extends FrameLayout {
    private LinearLayout content;
    private Menu menu;
    private View activeView;

    MenuItem.OnMenuItemClickListener listener;

    public BottomBar(Context context) {
        super(context);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dasd", "dsad");
            }
        });
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.carbon_bottombar, this);
        content = (LinearLayout) findViewById(R.id.carbon_bottomBarContent);
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(new CarbonContextWrapper(getContext()));
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        content.removeAllViews();
        content.setWeightSum(menu.size());
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            final View view = View.inflate(getContext(), R.layout.carbon_bottombar_item, null);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view == activeView)
                        return;
                    if (activeView != null) {
                        deselectItem(activeView);
                    }
                    selectItem(view);
                    if (listener != null)
                        listener.onMenuItemClick(item);
                }
            });
            ImageView icon = (ImageView) view.findViewById(R.id.carbon_bottomIcon);
            icon.setTint(new DefaultColorStateList(getContext()));
            icon.setImageDrawable(item.getIcon());
            TextView text = (TextView) view.findViewById(R.id.carbon_bottomText);
            text.setTextColor(new DefaultColorStateList(getContext()));
            text.setText(item.getTitle());
            content.addView(view, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
    }

    private void selectItem(final View item) {
        activeView = item;
        final ImageView icon = (ImageView) item.findViewById(R.id.carbon_bottomIcon);
        icon.setSelected(true);
        final TextView text = (TextView) item.findViewById(R.id.carbon_bottomText);
        text.setSelected(true);
        ValueAnimator animator = ValueAnimator.ofFloat(1, getResources().getDimension(R.dimen.carbon_bottomBarActiveTextSize) / getResources().getDimension(R.dimen.carbon_bottomBarInactiveTextSize));
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            text.setScaleX((Float) animation.getAnimatedValue());
            text.setScaleY((Float) animation.getAnimatedValue());
            text.postInvalidate();
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(0, -getResources().getDimension(R.dimen.carbon_1dip) * 2);
        animator2.setDuration(200);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> {
            icon.setTranslationY((Float) animation.getAnimatedValue());
            icon.postInvalidate();
        });
        animator2.start();
    }

    private void deselectItem(final View item) {
        final ImageView icon = (ImageView) item.findViewById(R.id.carbon_bottomIcon);
        icon.setSelected(false);
        final TextView text = (TextView) item.findViewById(R.id.carbon_bottomText);
        text.setSelected(false);
        ValueAnimator animator = ValueAnimator.ofFloat(getResources().getDimension(R.dimen.carbon_bottomBarActiveTextSize) / getResources().getDimension(R.dimen.carbon_bottomBarInactiveTextSize), 1);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            text.setScaleX((Float) animation.getAnimatedValue());
            text.setScaleY((Float) animation.getAnimatedValue());
            text.postInvalidate();
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(-getResources().getDimension(R.dimen.carbon_1dip) * 2, 0);
        animator2.setDuration(200);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> {
            icon.setTranslationY((Float) animation.getAnimatedValue());
            icon.postInvalidate();
        });
        animator2.start();
    }

    public int getSelectedIndex() {
        if (activeView == null)
            return -1;
        return content.indexOfChild(activeView);
    }

    public void setSelectedIndex(int index) {
        if (activeView != null)
            deselectItem(activeView);
        selectItem(content.getChildAt(index));
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.selectedIndex = getSelectedIndex();

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

        this.setSelectedIndex(ss.selectedIndex);
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        int selectedIndex;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(BottomBar.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.selectedIndex = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeInt(this.selectedIndex);
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
