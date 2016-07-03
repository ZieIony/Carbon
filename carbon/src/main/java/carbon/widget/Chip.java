package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2015-12-19.
 */
public class Chip extends LinearLayout {
    private ImageView icon;
    private TextView title;
    private ImageView close;
    private OnRemoveListener onRemoveListener;

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
        title = (TextView) findViewById(R.id.carbon_chipText);
        icon = (ImageView) findViewById(R.id.carbon_chipIcon);
        close = (ImageView) findViewById(R.id.carbon_chipClose);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRemoveListener != null)
                    onRemoveListener.onDismiss();
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Chip, defStyleAttr, 0);

            setCornerRadius((int) a.getDimension(R.styleable.Chip_carbon_cornerRadius, 0));  // this shouldn't be necessary
            setText(a.getString(R.styleable.Chip_android_text));
            int iconRes = a.getResourceId(R.styleable.Chip_carbon_icon, 0);
            if (iconRes != 0) {
                setIcon(iconRes);
            } else {
                setIconVisible(false);
            }
            int color = a.getColor(R.styleable.Chip_android_background, 0);
            setBackgroundColor(color);

            a.recycle();

            //Carbon.initElevation(this, a, R.styleable.Chip_carbon_elevation);
        }
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
        setIconVisible(iconRes != 0);
    }

    public void setIcon(Drawable drawable) {
        icon.setImageDrawable(drawable);
        setIconVisible(drawable != null);
    }

    public void setIcon(Bitmap bitmap) {
        icon.setImageBitmap(bitmap);
        setIconVisible(bitmap != null);
    }

    public Drawable getIcon() {
        return icon.getDrawable();
    }

    public View getIconView() {
        return icon;
    }

    public void setIconVisible(boolean visible) {
        icon.setVisibilityImmediate(visible ? VISIBLE : GONE);
        setPadding(visible ? 0 : (int) getResources().getDimension(R.dimen.carbon_chipPadding), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    public void setRemovable(boolean removable) {
        close.setVisibilityImmediate(removable ? VISIBLE : GONE);
        setPadding(getPaddingLeft(), getPaddingTop(), removable ? 0 : (int) getResources().getDimension(R.dimen.carbon_chipPadding), getPaddingBottom());
    }

    public boolean isRemovable() {
        return close.getVisibility() == VISIBLE;
    }

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

}
