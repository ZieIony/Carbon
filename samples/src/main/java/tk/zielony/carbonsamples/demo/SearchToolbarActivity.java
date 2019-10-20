package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.recycler.RowListAdapter;
import carbon.widget.FrameLayout;
import carbon.widget.RecyclerView;
import carbon.widget.SearchDataProvider;
import carbon.widget.SearchEditText;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

@ActivityAnnotation(layout = R.layout.activity_searchtoolbar, title = R.string.searchToolbarActivity_title)
public class SearchToolbarActivity extends ThemedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        List<DefaultAvatarTextItem> items = randomData.generateList(DefaultAvatarTextItem.class, 20);

        RowListAdapter<DefaultAvatarTextItem> adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow::new);
        adapter.setItems(items);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        SearchEditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setDataProvider(new SearchDataProvider<DefaultAvatarTextItem>() {
            @Override
            public int getItemCount() {
                return items.size();
            }

            @Override
            public DefaultAvatarTextItem getItem(int i) {
                return items.get(i);
            }

            @Override
            public String[] getItemWords(DefaultAvatarTextItem item) {
                return new String[]{item.getText()};
            }
        });
        searchEditText.setOnFilterListener(new SearchEditText.OnFilterListener<DefaultAvatarTextItem>() {
            @Override
            public void onFilter(List<DefaultAvatarTextItem> filterResults) {
                adapter.setItems(filterResults != null ? filterResults : items);
            }
        });

        findViewById(R.id.clear).setOnClickListener(v -> searchEditText.setText(""));

        FrameLayout searchBar = findViewById(R.id.searchbar);
        View searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v -> {
            searchBar.setVisibility(View.VISIBLE);
            int[] setLocation = new int[2];
            searchBar.getLocationOnScreen(setLocation);
            int[] sbLocation = new int[2];
            searchButton.getLocationOnScreen(sbLocation);
            Animator animator = searchBar.createCircularReveal(sbLocation[0] - setLocation[0] + v.getWidth() / 2, searchBar.getHeight() / 2, 0, searchBar.getWidth());
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.start();
            searchEditText.requestFocus();
        });
        findViewById(R.id.close).setOnClickListener(v -> {
            int[] setLocation = new int[2];
            searchBar.getLocationOnScreen(setLocation);
            int[] sbLocation = new int[2];
            searchButton.getLocationOnScreen(sbLocation);
            Animator animator = searchBar.createCircularReveal(sbLocation[0] - setLocation[0] + v.getWidth() / 2, searchBar.getHeight() / 2, searchBar.getWidth(), 0);
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    searchBar.setVisibility(View.INVISIBLE);
                }
            });
            animator.start();
            searchEditText.clearFocus();
        });
    }
}
