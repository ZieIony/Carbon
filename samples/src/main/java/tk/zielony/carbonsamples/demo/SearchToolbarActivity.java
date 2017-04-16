package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;

import carbon.widget.LinearLayout;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class SearchToolbarActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtoolbar);

        LinearLayout searchEditText = (LinearLayout) findViewById(R.id.searchbar);
        float margin = getResources().getDimension(R.dimen.carbon_toolbarItemMargin);
        findViewById(R.id.search).setOnClickListener(v -> {
            searchEditText.setVisibility(View.VISIBLE);
            searchEditText.startReveal((int) (searchEditText.getWidth() - v.getWidth() / 2 - margin), searchEditText.getHeight() / 2, 0, searchEditText.getWidth());
        });
        findViewById(R.id.close).setOnClickListener(v -> {
            Animator animator = searchEditText.startReveal((int) (searchEditText.getWidth() - v.getWidth() / 2 - margin), searchEditText.getHeight() / 2, searchEditText.getWidth(), 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    searchEditText.setVisibility(View.INVISIBLE);
                }
            });
        });
    }
}
