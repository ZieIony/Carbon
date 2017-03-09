package carbon.component;

import android.graphics.drawable.Drawable;

public class DefaultIconPasswordItem implements IconPasswordItem {
    private Drawable icon;
    private String hint,text;

    public DefaultIconPasswordItem(Drawable icon, String hint,String text) {
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
