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
import carbon.shadow.ShadowView;

/**
 * Created by Marcin on 2014-12-13.
 */
public class Toolbar extends LinearLayout implements ShadowView {
    private ViewGroup content;
    private ImageView icon;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_toolbarStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.carbon_toolbar, this);
        content = (ViewGroup) findViewById(R.id.carbon_toolbarContent);
        icon = (ImageView) findViewById(R.id.carbon_icon);

        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).onBackPressed();
            }
        });

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyle, 0);
        setElevation(a.getDimension(R.styleable.Toolbar_carbon_elevation, 0));  // this shouldn't be necessary
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
        TextView title = (TextView) findViewById(R.id.carbon_toolbarTitle);
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
        TextView title = (TextView) findViewById(R.id.carbon_toolbarTitle);
        return (String) title.getText();
    }

    public void setIcon(int iconRes) {
        if (iconRes == 0) {
            icon.setVisibility(GONE);
        } else {
            icon.setImageResource(iconRes);
            icon.setVisibility(VISIBLE);
        }
    }

    public void setIcon(Drawable drawable) {
        if (drawable == null) {
            icon.setVisibility(GONE);
        } else {
            icon.setImageDrawable(drawable);
            icon.setVisibility(VISIBLE);
        }
    }

    public void setIcon(Bitmap bitmap) {
        if (bitmap == null) {
            icon.setVisibility(GONE);
        } else {
            icon.setImageBitmap(bitmap);
            icon.setVisibility(VISIBLE);
        }
    }

    public void setIconVisible(boolean visible) {
        icon.setVisibility(visible ? VISIBLE : GONE);
    }
}
