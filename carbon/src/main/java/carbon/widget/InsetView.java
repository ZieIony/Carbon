package carbon.widget;

/**
 * Created by Marcin on 2015-04-11.
 */
public interface InsetView {
    public static int INSET_NULL = -1;

    void setInset(int touchMarginAll, int touchMarginAll1, int touchMarginAll2, int touchMarginAll3);

    void setInsetColor(int color);
}
