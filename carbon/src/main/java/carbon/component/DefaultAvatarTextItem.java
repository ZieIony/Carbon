package carbon.component;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2017-03-06.
 */

public class DefaultAvatarTextItem implements AvatarTextItem {
    private Drawable avatar;
    private String text;

    public DefaultAvatarTextItem() {
    }

    public DefaultAvatarTextItem(Drawable avatar, String text) {
        this.avatar = avatar;
        this.text = text;
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
}
