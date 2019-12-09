package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.View;

import com.annimon.stream.Stream;

import java.util.Random;

import carbon.widget.Chip;
import carbon.widget.EditText;
import carbon.widget.FlowLayout;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.DataContext;
import tk.zielony.randomdata.person.DrawableAvatarGenerator;
import tk.zielony.randomdata.person.Gender;
import tk.zielony.randomdata.person.StringFirstNameGenerator;

@SampleAnnotation(layoutId = R.layout.activity_flowlayout, titleId = R.string.flowLayoutActivity_title, iconId = R.drawable.ic_wrap_text_black_24dp)
public class FlowLayoutActivity extends ThemedActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        DrawableAvatarGenerator avatarGenerator = new DrawableAvatarGenerator(this);
        StringFirstNameGenerator nameGenerator = new StringFirstNameGenerator(Gender.Both, false, false);

        Random random = new Random();
        FlowLayout layout = findViewById(R.id.flowLayout);
        Stream.of(layout.getViews()).filter(v -> v instanceof Chip).forEach(v -> {
            final Chip chip = (Chip) v;
            DataContext dataContext = new DataContext();
            chip.setText(nameGenerator.next(dataContext));
            if (random.nextBoolean())
                chip.setIcon(avatarGenerator.next(dataContext));
            chip.setRemovable(random.nextBoolean());
            chip.setOnRemoveListener(() -> {
                chip.setVisibility(View.GONE);
            });
        });

        EditText addChip = findViewById(R.id.addChip);
        addChip.setOnEditorActionListener((textView, i, keyEvent) -> {
            Chip chip = new Chip(FlowLayoutActivity.this);
            chip.setText(addChip.getText());
            chip.setRemovable(random.nextBoolean());
            chip.setSelected(random.nextBoolean());
            layout.addView(chip);
            addChip.setText("");
            return true;
        });
    }
}
