package carbon.component;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-03-06.
 */

public class DefaultAvatarTextSubtextDateItem implements AvatarTextSubtextDateItem {
    private Drawable avatar;
    private String text;
    private String subtext;
    private String date;

    public DefaultAvatarTextSubtextDateItem() {
    }

    public DefaultAvatarTextSubtextDateItem(Drawable avatar, String text, String subtext, String date) {
        this.avatar = avatar;
        this.text = text;
        this.subtext = subtext;
        this.date = date;
    }

    @Override
    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
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
