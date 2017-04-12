package carbon.component;

import android.graphics.drawable.Drawable;

public class DefaultImageTextSubtextDateItem implements ImageTextSubtextDateItem {
    private Drawable image;
    private String text, subtext, date;

    public DefaultImageTextSubtextDateItem() {
    }

    public DefaultImageTextSubtextDateItem(Drawable image, String text, String subtext, String date) {
        this.image = image;
        this.text = text;
        this.subtext = subtext;
        this.date = date;
    }

    @Override
    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
