package carbon.widget;

import java.util.List;

/**
 * Created by Marcin on 2017-02-12.
 */

public class ListSearchDataProvider<Type> implements SearchEditText.SearchDataProvider<Type> {
    private List<Type> data;

    public ListSearchDataProvider(List<Type> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Type getItem(int i) {
        return data.get(i);
    }

    @Override
    public String[] getItemWords(int i) {
        return new String[]{data.get(i).toString()};
    }
}
