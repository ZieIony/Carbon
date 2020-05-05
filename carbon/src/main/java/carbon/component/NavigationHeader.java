package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import carbon.databinding.CarbonNavigationHeaderBinding;
import carbon.widget.FrameLayout;

public class NavigationHeader extends FrameLayout {
    private CarbonNavigationHeaderBinding binding;

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
        initNavigationHeader();
    }

    public NavigationHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationHeader();
    }

    public NavigationHeader(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationHeader();
    }

    public NavigationHeader(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initNavigationHeader();
    }

    private void initNavigationHeader() {
        binding = CarbonNavigationHeaderBinding.inflate(LayoutInflater.from(getContext()));
    }

    public void setItem(Item item) {
        binding.carbonAvatar.setImageDrawable(item.icon);
        binding.carbonText.setText(item.text);
        binding.carbonSubtext.setText(item.subtext);
    }
}
