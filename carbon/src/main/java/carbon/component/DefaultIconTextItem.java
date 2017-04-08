package carbon.component;

import android.graphics.drawable.Drawable;

public class DefaultIconTextItem implements IconTextItem {
    private Drawable drawable;

    private String text;

    public DefaultIconTextItem(Drawable drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }

    public Drawable getIcon() {
        return drawable;
    }

    @Override
    public String getText() {
        return text;
    }
}
