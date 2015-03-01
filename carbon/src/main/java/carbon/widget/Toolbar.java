package carbon.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
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
    private SVGView upIcon;

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
        upIcon = (SVGView) findViewById(R.id.carbon_upIcon);

        upIcon.setOnClickListener(new OnClickListener() {
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
    public void addView(View child) {
        if (content != null) {
            content.addView(child);
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (content != null) {
            content.addView(child, index);
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (content != null) {
            content.addView(child, params);
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
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

    public void setUpIcon(int iconRes) {
        if (iconRes == 0) {
            upIcon.setVisibility(GONE);
        } else {
            upIcon.setSVGResource(iconRes);
            upIcon.setVisibility(VISIBLE);
        }
    }

    public void setUpIconVisible(boolean visible) {
        upIcon.setVisibility(visible ? VISIBLE : GONE);
    }
}
