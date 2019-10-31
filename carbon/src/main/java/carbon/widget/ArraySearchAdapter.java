package carbon.widget;

public class ArraySearchAdapter<Type> extends SearchAdapter<Type> {
    private Type[] data;

    public ArraySearchAdapter(Type[] data) {
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
