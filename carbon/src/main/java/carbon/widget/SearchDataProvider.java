package carbon.widget;

public interface SearchDataProvider<Type> {

    int getItemCount();

    Type getItem(int i);

    String[] getItemWords(Type item);
}
