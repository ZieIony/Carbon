package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Checkable;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;

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

    private FrameLayout content;
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

    public Chip(Context context, CharSequence text) {
        super(context, null, R.attr.carbon_chipStyle);
        initChip(null, R.attr.carbon_chipStyle);
        setText(text);
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
        content = findViewById(R.id.carbon_chipContent);
        check = findViewById(R.id.carbon_chipCheck);
        close = findViewById(R.id.carbon_chipClose);

        close.setOnClickListener(v -> {
            if (onRemoveListener != null)
                onRemoveListener.onDismiss();
        });

        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Chip, defStyleAttr, R.style.carbon_Chip);

        setText(a.getString(R.styleable.Chip_android_text));
        setIcon(Carbon.getDrawable(this, a, R.styleable.Chip_carbon_icon, 0));
        setRemovable(a.getBoolean(R.styleable.Chip_carbon_removable, false));
        setChecked(a.getBoolean(R.styleable.Chip_android_checked, false));
        setTooltipText(a.getText(R.styleable.Chip_carbon_tooltipText));

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

    @Deprecated
    public void setText(String text) {
        setText((CharSequence) text);
    }

    public void setText(CharSequence text) {
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
        content.removeAllViews();
        if (iconRes == 0) {
            content.setVisibility(GONE);
            return;
        }
        content.setVisibility(VISIBLE);
        ImageView icon = new ImageView(getContext());
        content.addView(icon);
        icon.setImageResource(iconRes);
    }

    public void setIcon(Drawable drawable) {
        content.removeAllViews();
        if (drawable == null) {
            content.setVisibility(GONE);
            return;
        }
        content.setVisibility(VISIBLE);
        ImageView icon = new ImageView(getContext());
        content.addView(icon);
        icon.setImageDrawable(drawable);
    }

    public void setIcon(Bitmap bitmap) {
        content.removeAllViews();
        if (bitmap == null) {
            content.setVisibility(GONE);
            return;
        }
        content.setVisibility(VISIBLE);
        ImageView icon = new ImageView(getContext());
        content.addView(icon);
        icon.setImageBitmap(bitmap);
    }

    @Deprecated
    public Drawable getIcon() {
        if (content.getChildCount() > 0 && content.getChildAt(0) instanceof android.widget.ImageView)
            return ((android.widget.ImageView) content.getChildAt(0)).getDrawable();
        return null;
    }

    @Deprecated
    public View getIconView() {
        if (content.getChildCount() > 0 && content.getChildAt(0) instanceof android.widget.ImageView)
            return content.getChildAt(0);
        return null;
    }

    public View getContentView() {
        if (content.getChildCount() > 0)
            return content.getChildAt(0);
        return null;
    }

    public void setContentView(View view) {
        content.removeAllViews();
        if (view != null) {
            content.setVisibility(VISIBLE);
            content.addView(view);
        } else {
            content.setVisibility(GONE);
        }
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


    // -------------------------------
    // tooltip
    // -------------------------------

    public void setTooltipText(CharSequence text) {
        if (text != null) {
            setOnLongClickListener(v -> {
                Label tooltip = (Label) LayoutInflater.from(getContext()).inflate(R.layout.carbon_tooltip, null);
                tooltip.setText(text);
                PopupWindow window = new PopupWindow(tooltip);
                window.show(this, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                new Handler(Looper.getMainLooper()).postDelayed(window::dismiss, AnimUtils.TOOLTIP_DURATION);
                return true;
            });
        } else if (isLongClickable()) {
            setOnLongClickListener(null);
        }
    }
}
