package carbon.recycler;

import android.view.View;

/**
 * Created by Marcin on 2016-06-09.
 */
public interface Row<Type> {
    View getView();
    void bind(Type data);
}
