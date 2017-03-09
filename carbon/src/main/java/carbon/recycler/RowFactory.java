package carbon.recycler;

import android.view.ViewGroup;

/**
 * Created by Marcin on 2016-08-16.
 */

public interface RowFactory {
    Component create(ViewGroup parent);
}
