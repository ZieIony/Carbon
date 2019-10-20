package carbon.internal;

import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;

import carbon.widget.SearchEditText;

public class SearchHelper {
    private SearchHelper() {
    }

    public static Spannable highlightMatchedChars(String text, String query, SearchEditText.MatchMode matchMode, ParcelableSpan span) {
        SpannableString string = new SpannableString(text);
        if (matchMode == SearchEditText.MatchMode.START || matchMode == SearchEditText.MatchMode.ADJACENT) {
            int index = text.indexOf(query);
            if (index != -1)
                string.setSpan(span, index, index + query.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (matchMode == SearchEditText.MatchMode.NONADJACENT) {
            int i = 0, j = 0;
            for (; i < text.length() && j < query.length(); i++) {
                if (text.charAt(i) == query.charAt(j)) {
                    string.setSpan(span, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    j++;
                }
            }
        }
        return string;
    }

    public static boolean nonadjacentMatch(String text, String query) {  // text: 'lemon', query: 'ln'
        int i = 0, j = 0;
        for (; i < text.length() && j < query.length(); i++) {
            if (text.charAt(i) == query.charAt(j))
                j++;
        }
        return j == query.length();
    }

}
