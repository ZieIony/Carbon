package carbon.widget;

public class ArraySearchDataProvider<Type> implements SearchDataProvider<Type> {
    private Type[] data;

    public ArraySearchDataProvider(Type[] data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    @Override
    public Type getItem(int i) {
        return data[i];
    }

    @Override
    public String[] getItemWords(Type item) {
        return new String[]{item.toString()};
    }
}
