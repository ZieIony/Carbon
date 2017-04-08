package tk.zielony.carbonsamples.feature.scroll;

public interface ScrollChild {
    int onNestedScrollByY(int y);

    int getNestedScrollRange();

    int getNestedScrollY();
}
