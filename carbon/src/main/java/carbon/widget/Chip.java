package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Checkable;

import carbon.R;

public class Chip extends LinearLayout implements Checkable {

    /**
     * Interface definition for a callback to be invoked when the checked state of a chip
     * changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a chip has changed.
         *
         * @param chip      The chip whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(Chip chip, boolean isChecked);
    }

    private ImageView icon;
    private ImageView check;
    private TextView title;
    private ImageView close;
    private OnRemoveListener onRemoveListener;
    private boolean checkedState = false;
    private OnCheckedChangeListener onCheckedChangeListener;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public interface OnRemoveListener {
        void onDismiss();
    }

    public Chip(Context context) {
        super(context, null, R.attr.carbon_chipStyle);
        initChip(null, R.attr.carbon_chipStyle);
    }

    public Chip(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_chipStyle);
        initChip(attrs, R.attr.carbon_chipStyle);
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChip(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Chip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChip(attrs, defStyleAttr);
    }

    private void initChip(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.carbon_chip, this);
        title = findViewById(R.id.carbon_chipText);
        icon = findViewById(R.id.carbon_chipIcon);
        check = findViewById(R.id.carbon_chipCheck);
        close = findViewById(R.id.carbon_chipClose);

        close.setOnClickListener(v -> {
            if (onRemoveListener != null)
                onRemoveListener.onDismiss();
        });

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Chip, defStyleAttr, R.style.carbon_Chip);

        setCornerRadius(a.getDimension(R.styleable.Chip_carbon_cornerRadius, 0));  // this shouldn't be necessary
        setText(a.getString(R.styleable.Chip_android_text));
        setIcon(a.getResourceId(R.styleable.Chip_carbon_icon, 0));
        setRemovable(a.getBoolean(R.styleable.Chip_carbon_removable, false));

        a.recycle();
    }

    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    public boolean performClick() {
        toggle();

        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(this, isChecked());

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
        return checkedState;
    }

    /**
     * <p>Changes the checked state of this chip.</p>
     *
     * @param checked true to check the chip, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (this.checkedState != checked) {
            checkedState = checked;
            check.setVisibility(checked ? VISIBLE : GONE);
            refreshDrawableState();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /**
     * Register a callback to be invoked when the checked state of this chip changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    public void setText(String text) {
        if (text != null) {
            title.setText(text);
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
    }

    public void setText(int resId) {
        setText(getResources().getString(resId));
    }

    public String getText() {
        return (String) title.getText();
    }

    public View getTitleView() {
        return title;
    }

    public void setIcon(int iconRes) {
        icon.setImageResource(iconRes);
        icon.setVisibility(iconRes != 0 ? VISIBLE : GONE);
    }

    public void setIcon(Drawable drawable) {
        icon.setImageDrawable(drawable);
        icon.setVisibility(drawable != null ? VISIBLE : GONE);
    }

    public void setIcon(Bitmap bitmap) {
        icon.setImageBitmap(bitmap);
        icon.setVisibility(bitmap != null ? VISIBLE : GONE);
    }

    public Drawable getIcon() {
        return icon.getDrawable();
    }

    public View getIconView() {
        return icon;
    }

    public void setRemovable(boolean removable) {
        close.setVisibility(removable ? VISIBLE : GONE);
    }

    public boolean isRemovable() {
        return close.getVisibility() == VISIBLE;
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

}
