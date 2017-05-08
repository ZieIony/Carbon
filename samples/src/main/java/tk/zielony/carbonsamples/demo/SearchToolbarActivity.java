package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class SearchToolbarActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtoolbar);

        LinearLayout searchEditText = (LinearLayout) findViewById(R.id.searchbar);
        View searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v -> {
            searchEditText.setVisibility(View.VISIBLE);
            int[] setLocation = new int[2];
            searchEditText.getLocationOnScreen(setLocation);
            int[] sbLocation = new int[2];
            searchButton.getLocationOnScreen(sbLocation);
            Animator animator = searchEditText.createCircularReveal(sbLocation[0] - setLocation[0] + v.getWidth() / 2, searchEditText.getHeight() / 2, 0, searchEditText.getWidth());
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        });
        findViewById(R.id.close).setOnClickListener(v -> {
            int[] setLocation = new int[2];
            searchEditText.getLocationOnScreen(setLocation);
            int[] sbLocation = new int[2];
            searchButton.getLocationOnScreen(sbLocation);
            Animator animator = searchEditText.createCircularReveal(sbLocation[0] - setLocation[0] + v.getWidth() / 2, searchEditText.getHeight() / 2, searchEditText.getWidth(), 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    searchEditText.setVisibility(View.INVISIBLE);
                }
            });
            animator.start();
        });
    }
}
