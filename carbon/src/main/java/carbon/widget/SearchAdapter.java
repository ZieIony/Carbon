package carbon.widget;

import carbon.internal.SearchHelper;

public abstract class SearchAdapter<Type> {

    public abstract int getItemCount();

    public abstract Type getItem(int i);

    public abstract String[] getItemWords(Type item);

    public boolean filterItem(SearchEditText.SearchSettings settings, String query, Type item) {
        String[] itemWords = getItemWords(item);
        if (itemWords == null)
            return false;
        for (String itemWord : itemWords) {
            String itemText = itemWord.toLowerCase();
            if (settings.matchMode == SearchEditText.MatchMode.START && itemText.indexOf(query) == 0) {
                return true;
            } else if (settings.matchMode == SearchEditText.MatchMode.ADJACENT && itemText.contains(query)) {
                return true;
            } else if (settings.matchMode == SearchEditText.MatchMode.NONADJACENT && SearchHelper.nonadjacentMatch(itemText, query)) {
                return true;
            }
        }
        return false;
    }
}
