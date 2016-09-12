package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.widget.AutoCompleteTextView;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-26.
 */
public class AutoCompleteDemo extends Activity {
    String[] fruits = {"Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.results);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        search.setDataProvider(new AutoCompleteTextView.AutoCompleteDataProvider() {
            List<AutoCompleteTextView.FilteringResult> filteredItems = new ArrayList<>();

            @Override
            public List<AutoCompleteTextView.FilteringResult> filter(AutoCompleteTextView.Word word) {
                filteredItems.clear();
                if (word.length() == 0)
                    return filteredItems;
                String text = word.preCursor.toLowerCase();
                for (String item : fruits) {
                    if (item.length() == text.length())
                        continue;
                    item = item.toLowerCase();
                    int index = item.indexOf(text);
                    if (index == 0) {
                        Spannable spannable = new SpannableStringBuilder(item);
                        spannable.setSpan(new AutoCompleteTextView.HintSpan(search.getCurrentHintTextColor()), text.length(), item.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        filteredItems.add(new AutoCompleteTextView.FilteringResult(AutoCompleteTextView.FILTERING_START, spannable));
                    } else if (index > 0) {
                        Spannable spannable = new SpannableStringBuilder(item);
                        spannable.setSpan(new AutoCompleteTextView.HintSpan(search.getCurrentHintTextColor()), 0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (index + text.length() < item.length())
                            spannable.setSpan(new AutoCompleteTextView.HintSpan(search.getCurrentHintTextColor()), index + text.length(), item.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        filteredItems.add(new AutoCompleteTextView.FilteringResult(AutoCompleteTextView.FILTERING_MIDDLE, spannable));
                    } else {
                        Spannable spannable = partialMatch(item, word);
                        if (spannable != null)
                            filteredItems.add(new AutoCompleteTextView.FilteringResult(AutoCompleteTextView.FILTERING_PARTIAL, spannable));
                    }
                }
                Collections.sort(filteredItems);
                return filteredItems;
            }

            private Spannable partialMatch(String item, AutoCompleteTextView.Word word) {  // item: 'lemon', text: 'le'
                Spannable spannable = new SpannableStringBuilder(item);
                int i = 0, j = 0;
                String text = word.toString().toLowerCase();
                for (; i < item.length() && j < text.length(); i++) {
                    if (item.charAt(i) == text.charAt(j)) {
                        j++;
                    } else {
                        spannable.setSpan(new AutoCompleteTextView.HintSpan(search.getCurrentHintTextColor()), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (j == text.length())
                    return spannable;
                return null;
            }
        });

        recycler.setAdapter(search.getAdapter());

        /*adapter.setOnAutoCompleteListener(new AutoCompleteTextView.OnAutoCompleteListener() {
            @Override
            public void onAutoComplete() {
                recyclerLayoutParams.height = (int) (Math.min(3, adapter.getItemCount()) * getResources().getDimension(R.dimen.carbon_toolbarHeight));
                recycler.setLayoutParams(recyclerLayoutParams);
                dropDown.setVisibility(View.VISIBLE);
            }
        });*/

        /*adapter.setOnHintClicked(new OnHintClicked() {

            @Override
            public void onHintClicked(String hint) {
                search.performCompletion(hint);
                dropDown.setVisibility(View.GONE);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        View editText = getCurrentFocus();
        if (editText != null && editText instanceof AutoCompleteTextView) {
            editText.clearFocus();
            return;
        }
        super.onBackPressed();
    }
}
