package carbon.widget;

import java.util.List;

public class ListSearchAdapter<Type> extends SearchAdapter<Type> {
    private List<Type> data;

    public ListSearchAdapter(List<Type> data) {
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
