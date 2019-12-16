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
        String queryText = settings.matchCase ? query : query.toLowerCase();
        for (String itemWord : itemWords) {
            String itemText = settings.matchCase ? itemWord : itemWord.toLowerCase();
            if (settings.matchMode == SearchEditText.MatchMode.START && itemText.indexOf(queryText) == 0) {
                return true;
            } else if (settings.matchMode == SearchEditText.MatchMode.ADJACENT && itemText.contains(queryText)) {
                return true;
            } else if (settings.matchMode == SearchEditText.MatchMode.NONADJACENT && SearchHelper.nonadjacentMatch(itemText, queryText)) {
                return true;
            }
        }
        return false;
    }
}
