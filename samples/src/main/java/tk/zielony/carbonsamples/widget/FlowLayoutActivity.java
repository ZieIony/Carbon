package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.animation.AnimUtils;
import carbon.widget.Chip;
import carbon.widget.FlowLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-12-19.
 */
public class FlowLayoutActivity extends Activity {
    private static List<String> fruits = new ArrayList<>(Arrays.asList("Strawberry", "Apple", "Orange", "Lemon", "Beer", "Lime", "Watermelon", "Blueberry", "Plum"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);

        FlowLayout layout = (FlowLayout) findViewById(R.id.flowLayout);
        for (int i = 0; i < layout.getChildCount() - 1; i++) {
            final Chip chip = (Chip) layout.getChildAt(i);
            chip.setText(fruits.get(i % fruits.size()));
            if (i % 3 != 1) {
                chip.setIconVisible(true);
                String image = "http://lorempixel.com/100/100/people/#" + System.currentTimeMillis();
                Picasso.with(this).load(image).into((ImageView) chip.getIconView());
            } else {
                chip.setIconVisible(false);
            }
            if (i % 3 != 2) {
                chip.setRemovable(true);
                chip.setOnRemoveListener(new Chip.OnRemoveListener() {
                    @Override
                    public void onDismiss() {
                        chip.setOutAnimation(AnimUtils.Style.Fade);
                        chip.setVisibility(View.GONE);
                    }
                });
            } else {
                chip.setRemovable(false);
            }
        }
    }
}
