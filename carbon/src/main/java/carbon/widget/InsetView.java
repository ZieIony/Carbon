package carbon.widget;

/**
 * Created by Marcin on 2015-04-11.
 */
public interface InsetView {
    int INSET_NULL = -1;

    void setInset(int left, int top, int right, int bottom);

    void setInsetColor(int color);
}
