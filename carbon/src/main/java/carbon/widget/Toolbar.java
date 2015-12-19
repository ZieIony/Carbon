package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

/**
 * Created by Marcin on 2014-12-13.
 */
public class Toolbar extends FrameLayout {
    private ViewGroup content;
    private ImageView icon;
    private TextView title;

    public Toolbar(Context context) {
        super(context);
        initToolbar(null, R.attr.carbon_toolbarStyle);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initToolbar(attrs, R.attr.carbon_toolbarStyle);
    }

    private void initToolbar(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.carbon_toolbar, this);
        content = (ViewGroup) findViewById(R.id.carbon_toolbarContent);
        title = (TextView) findViewById(R.id.carbon_toolbarTitle);
        icon = (ImageView) findViewById(R.id.carbon_toolbarIcon);

        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).onBackPressed();
            }
        });

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyle, 0);
        setElevation(a.getDimension(R.styleable.Toolbar_carbon_elevation, 0));  // this shouldn't be necessary
        setText(a.getString(R.styleable.Toolbar_android_text));
        int iconRes = a.getResourceId(R.styleable.Toolbar_carbon_icon, 0);
        if (iconRes != 0) {
            setIcon(iconRes);
        } else {
            setIconVisible(false);
        }
        int color = a.getColor(R.styleable.Toolbar_android_background, 0);
        setBackgroundColor(color);
        a.recycle();
    }

    @Override
    public void addView(@NonNull View child) {
        if (content != null) {
            content.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int index) {
        if (content != null) {
            content.addView(child, index);
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int width, int height) {
        if (content != null) {
            content.addView(child, width, height);
        } else {
            super.addView(child, width, height);
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
        icon.setVisibility(visible ? VISIBLE : GONE);
    }
}
