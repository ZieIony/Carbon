package carbon.widget;

import java.util.List;

public class ListSearchDataProvider<Type> implements SearchDataProvider<Type> {
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
    public String[] getItemWords(Type item) {
        return new String[]{item.toString()};
    }
}
