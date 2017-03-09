package carbon.component;

import android.graphics.drawable.Drawable;

public class DefaultIconEditTextItem implements IconEditTextItem {
    private Drawable icon;
    private String hint, text;

    public DefaultIconEditTextItem(Drawable icon, String hint, String text) {
        this.icon = icon;
        this.hint = hint;
        this.text = text;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
