package carbon.widget;

public class ArraySearchDataProvider<Type> implements SearchEditText.SearchDataProvider<Type> {
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
    public String[] getItemWords(int i) {
        return new String[]{data[i].toString()};
    }
}
