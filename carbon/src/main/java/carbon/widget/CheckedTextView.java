package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;
import android.widget.CompoundButton;

import carbon.R;
import carbon.drawable.CheckableDrawable;
import carbon.drawable.DefaultColorStateList;
import carbon.drawable.ripple.RippleDrawable;

/**
 * Created by Marcin on 2015-12-23.
 */
public class CheckedTextView extends TextView implements Checkable {
    private CheckableDrawable drawable;
    private float drawablePadding;

    public CheckedTextView(Context context) {
        super(context, null, R.attr.checkedTextViewStyle);
        initCheckedTextView(null, R.attr.checkedTextViewStyle);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.checkedTextViewStyle);
        initCheckedTextView(attrs, R.attr.checkedTextViewStyle);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCheckedTextView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCheckedTextView(attrs, defStyleAttr);
    }

    public void initCheckedTextView(AttributeSet attrs, int defStyleAttr) {
        CheckableDrawable d = new CheckableDrawable(getContext(), R.raw.carbon_checkbox_checked, R.raw.carbon_checkbox_unchecked, R.raw.carbon_checkbox_filled, new PointF(-0.09f, 0.11f));
        setButtonDrawable(d);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CheckedTextView, defStyleAttr, 0);

            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.CheckedTextView_android_drawablePadding) {
                    drawablePadding = a.getDimension(attr, 0);
                } else if (attr == R.styleable.RadioButton_android_checked) {
                    setCheckedImmediate(a.getBoolean(attr, false));
                }
            }

            a.recycle();
        }
    }

    private boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private boolean mChecked;
    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            //notifyViewAccessibilityStateChangedIfNeeded(
            //      AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }

            mBroadcasting = false;
        }
    }

    public void setCheckedImmediate(boolean checked) {
        setChecked(checked);
        drawable.setCheckedImmediate(checked);
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(CheckedTextView buttonView, boolean isChecked);
    }

    /**
     * Set the button graphic to a given Drawable
     *
     * @param d The Drawable to use as the button graphic
     */
    public void setButtonDrawable(CheckableDrawable d) {
        if (drawable != d) {
            if (drawable != null) {
                drawable.setCallback(null);
                unscheduleDrawable(drawable);
            }

            drawable = d;

            if (d != null) {
                d.setCallback(this);
                //d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                d.setVisible(getVisibility() == VISIBLE, false);
                setMinHeight(d.getIntrinsicHeight());
                applyButtonTint();
            }
        }
    }

    public void setTint(@Nullable ColorStateList list) {
        super.setTint(list);
        applyButtonTint();
    }

    @Override
    public void setTint(int color) {
        if (color == 0) {
            setTint(new DefaultColorStateList(getContext()));
        } else {
            setTint(ColorStateList.valueOf(color));
        }
    }

    public void setTintMode(@Nullable PorterDuff.Mode mode) {
        super.setTintMode(mode);
        applyButtonTint();
    }

    private void applyButtonTint() {
        if (drawable != null && getTint() != null && getTintMode() != null) {
            drawable = (CheckableDrawable) drawable.mutate();

            drawable.setTint(getTint());
            drawable.setTintMode(getTintMode());

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CheckedTextView.class.getName());
        event.setChecked(mChecked);
    }

    /*@Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CheckedTextView.class.getName());
        info.setCheckable(true);
        info.setChecked(mChecked);
    }*/

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (isLayoutRtl()) {
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
        if (!isLayoutRtl()) {
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
                    top = getHeight() - drawableHeight;
                    break;
                case Gravity.CENTER_VERTICAL:
                    top = (getHeight() - drawableHeight) / 2;
                    break;
                default:
                    top = 0;
            }
            final int bottom = top + drawableHeight;
            final int left = isLayoutRtl() ? getPaddingLeft() : getWidth() - drawableWidth - getPaddingRight();
            final int right = isLayoutRtl() ? drawableWidth + getPaddingLeft() : getWidth() - getPaddingRight();

            buttonDrawable.setBounds(left, top, right, bottom);

            final Drawable background = getBackground();
            if (background != null && background instanceof RippleDrawable) {
                //TODO: hotspotBounds
                // ((RippleDrawable)background).setHotspotBounds(left, top, right, bottom);
            }
        }

        super.onDraw(canvas);

        if (buttonDrawable != null) {
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
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (drawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            drawable.setState(myDrawableState);

            invalidate();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == drawable;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (drawable != null) drawable.jumpToCurrentState();
    }

    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "CheckedTextView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}
