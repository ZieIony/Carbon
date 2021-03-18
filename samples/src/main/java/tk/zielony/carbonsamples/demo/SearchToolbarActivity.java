package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import carbon.component.AvatarTextRow;
import carbon.component.DefaultAvatarTextItem;
import carbon.recycler.RowListAdapter;
import carbon.widget.FrameLayout;
import carbon.widget.ListSearchAdapter;
import carbon.widget.RecyclerView;
import carbon.widget.SearchEditText;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

@SampleAnnotation(
        layoutId = R.layout.activity_searchtoolbar,
        titleId = R.string.searchToolbarActivity_title,
        iconId = R.drawable.carbon_search
)
public class SearchToolbarActivity extends ThemedActivity {

    SearchEditText searchEditText;
    FrameLayout searchBar;
    View closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<DefaultAvatarTextItem> items = generateItems();

        RowListAdapter<DefaultAvatarTextItem> adapter = new RowListAdapter<>(items, AvatarTextRow::new);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setDataProvider(new ListSearchAdapter<DefaultAvatarTextItem>(items) {
            @NotNull
            @Override
            public String[] getItemWords(DefaultAvatarTextItem item) {
                return new String[]{item.getText()};
            }
        });
        searchEditText.setOnFilterListener((SearchEditText.OnFilterListener<DefaultAvatarTextItem>) filterResults -> {
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClicked((view, item, position) -> openSearch(view));

        closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(v -> closeSearch());
    }

    private void closeSearch() {
        int[] setLocation = new int[2];
        searchBar.getLocationOnScreen(setLocation);
        int[] sbLocation = new int[2];
        closeButton.getLocationOnScreen(sbLocation);
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

    private void openSearch(View view) {
        searchBar.setVisibility(View.VISIBLE);
        int[] setLocation = new int[2];
        searchBar.getLocationOnScreen(setLocation);
        int[] sbLocation = new int[2];
        view.getLocationOnScreen(sbLocation);
        Animator animator = searchBar.createCircularReveal(sbLocation[0] - setLocation[0] + view.getWidth() / 2, searchBar.getHeight() / 2, 0, searchBar.getWidth());
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        searchEditText.requestFocus();
    }

    @NotNull
    private List<DefaultAvatarTextItem> generateItems() {
        RandomData randomData = new RandomData();
        randomData.addGenerator(Drawable.class, new DrawableAvatarGenerator(this));
        randomData.addGenerator(String.class, new StringNameGenerator().withMatcher(f -> f.getName().equals("text")));
        return randomData.generateList(DefaultAvatarTextItem.class, 20);
    }
}
