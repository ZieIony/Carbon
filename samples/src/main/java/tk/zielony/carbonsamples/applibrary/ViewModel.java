package tk.zielony.carbonsamples.applibrary;

import java.util.Date;

/**
 * Created by Marcin on 2015-02-12.
 */
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
