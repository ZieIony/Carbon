package tk.zielony.carbonsamples.feature.scroll;

/**
 * Created by Marcin on 2017-02-08.
 */
public interface ScrollChild {
    int onNestedScrollByY(int y);

    int getNestedScrollRange();

    int getNestedScrollY();
}
