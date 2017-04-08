package tk.zielony.carbonsamples.applibrary;

import java.util.Date;

public class ViewModel {
    private String text = new Date().toString();
    private String image = "http://lorempixel.com/500/200/people/#" + System.currentTimeMillis();

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }
}
