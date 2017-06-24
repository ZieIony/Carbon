package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.widget.Chip;
import carbon.widget.FlowLayout;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class FlowLayoutActivity extends SamplesActivity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);

        Samples.initToolbar(this, getString(R.string.flowLayoutActivity_title));

        FlowLayout layout = findViewById(R.id.flowLayout);
        Stream.of(layout.getViews()).filter(v -> v instanceof Chip).forEach(v -> {
            final Chip chip = (Chip) v;
            chip.setText(fruits.get((int) (Math.random() * fruits.size())));
            if (Math.random() > 0.5) {
                chip.setIconVisible(true);
                String image = "http://lorempixel.com/100/100/people/#" + System.currentTimeMillis();
                Picasso.with(this).load(image).into((ImageView) chip.getIconView());
            } else {
                chip.setIconVisible(false);
            }
            if (Math.random() > 0.5) {
                chip.setRemovable(true);
                chip.setOnRemoveListener(() -> {
                    //       chip.setOutAnimator(AnimUtils.Style.Fade);
                    chip.setVisibility(View.GONE);
                });
            } else {
                chip.setRemovable(false);
            }
        });
    }
}
