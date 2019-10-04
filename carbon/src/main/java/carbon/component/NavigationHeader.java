package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import carbon.R;
import carbon.widget.FrameLayout;

public class NavigationHeader extends FrameLayout {
    private ViewDataBinding binding;

    public static class Item {
        private final Drawable icon;
        private final String text, subtext;

        public Item(Drawable icon, String text, String subtext) {
            this.icon = icon;
            this.text = text;
            this.subtext = subtext;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }

        public String getSubtext() {
            return subtext;
        }
    }

    public NavigationHeader(Context context) {
        super(context);
        initNavigationHeader(null, R.attr.carbon_navigationHeaderStyle);
    }

    public NavigationHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationHeader(attrs, R.attr.carbon_navigationHeaderStyle);
    }

    public NavigationHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationHeader(attrs, defStyleAttr);
    }

    public NavigationHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initNavigationHeader(attrs, defStyleAttr);
    }

    private void initNavigationHeader(AttributeSet attrs, int defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.carbon_navigation_header, this, true);
    }

    public void setItem(Item item) {
        binding.setVariable(carbon.BR.data, item);
    }
}
