package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowListAdapter;
import carbon.widget.FrameLayout;
import carbon.widget.ListSearchAdapter;
import carbon.widget.RecyclerView;
import carbon.widget.SearchEditText;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

@ActivityAnnotation(
        layout = R.layout.activity_searchtoolbar,
        title = R.string.searchToolbarActivity_title,
        icon = R.drawable.carbon_search
)
public class SearchToolbarActivity extends ThemedActivity {

    SearchEditText searchEditText;
    FrameLayout searchBar;
    View searchButton;
    View closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Serializable> items = generateItems();

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultAvatarTextItem.class, AvatarTextRow::new);
        adapter.putFactory(PaddingItem.class, PaddingRow::new);
        adapter.setItems(items);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setDataProvider(new ListSearchAdapter<Serializable>(items) {
            @Override
            public String[] getItemWords(Serializable item) {
                return item instanceof DefaultAvatarTextItem ? new String[]{((DefaultAvatarTextItem) item).getText()} : null;
            }

            @Override
            public boolean filterItem(SearchEditText.SearchSettings settings, String query, Serializable item) {
                return item instanceof PaddingItem || super.filterItem(settings, query, item);
            }
        });
        searchEditText.setOnFilterListener((SearchEditText.OnFilterListener<Serializable>) filterResults -> {
            if (filterResults == null) {
                adapter.setItems(items);
            } else if (filterResults.size() == 2) {
                adapter.setItems(new ArrayList<>());
            } else {
                adapter.setItems(filterResults);
            }
        });

        findViewById(R.id.clear).setOnClickListener(v -> searchEditText.setText(""));

        searchBar = findViewById(R.id.searchbar);
        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v -> openSearch());

        closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(v -> closeSearch());
    }

    private void closeSearch() {
        int[] setLocation = new int[2];
        searchBar.getLocationOnScreen(setLocation);
        int[] sbLocation = new int[2];
        searchButton.getLocationOnScreen(sbLocation);
        Animator animator = searchBar.createCircularReveal(sbLocation[0] - setLocation[0] + closeButton.getWidth() / 2, searchBar.getHeight() / 2, searchBar.getWidth(), 0);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchBar.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
        searchEditText.clearFocus();
    }

    private void openSearch() {
        searchBar.setVisibility(View.VISIBLE);
        int[] setLocation = new int[2];
        searchBar.getLocationOnScreen(setLocation);
        int[] sbLocation = new int[2];
        searchButton.getLocationOnScreen(sbLocation);
        Animator animator = searchBar.createCircularReveal(sbLocation[0] - setLocation[0] + searchButton.getWidth() / 2, searchBar.getHeight() / 2, 0, searchBar.getWidth());
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        searchEditText.requestFocus();
    }

    @NotNull
    private List<Serializable> generateItems() {
        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableAvatarGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text"))
        });
        List<Serializable> items = new ArrayList<>();
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        items.addAll(randomData.generateList(DefaultAvatarTextItem.class, 20));
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        return items;
    }
}
