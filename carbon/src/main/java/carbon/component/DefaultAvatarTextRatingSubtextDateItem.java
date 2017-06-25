package carbon.component;

import android.graphics.drawable.Drawable;

public class DefaultAvatarTextRatingSubtextDateItem implements AvatarTextRatingSubtextDateItem {
    private Drawable avatar;
    private String text;
    private int rating;
    private String subtext;
    private String date;

    public DefaultAvatarTextRatingSubtextDateItem() {
    }

    public DefaultAvatarTextRatingSubtextDateItem(Drawable avatar, String text, int rating, String subtext, String date) {
        this.avatar = avatar;
        this.text = text;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
