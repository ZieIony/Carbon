package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.annimon.stream.Stream;

import java.util.List;

import carbon.component.DefaultImageTextSubtextDateItem;
import carbon.component.ImageTextSubtextDateRow;
import carbon.dialog.ListDialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DrawableImageGenerator;
import tk.zielony.randomdata.common.StringDateGenerator;
import tk.zielony.randomdata.common.TextGenerator;
import tk.zielony.randomdata.person.StringNameGenerator;

@SampleAnnotation(layoutId = R.layout.activity_listdialog, titleId = R.string.listDialogActivity_title)
public class ListDialogActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        EditText titleText = findViewById(R.id.titleText);

        List<DefaultImageTextSubtextDateItem> items = Stream.generate(DefaultImageTextSubtextDateItem::new).limit(9).toList();

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new DrawableImageGenerator(this),
                new StringNameGenerator().withMatcher(f -> f.getName().equals("text") && f.getDeclaringClass().equals(DefaultImageTextSubtextDateItem.class)),
                new TextGenerator().withMatcher(f -> f.getName().equals("subtext")),
                new StringDateGenerator()
        });
        randomData.fill(items);

        findViewById(R.id.button).setOnClickListener(view -> {
            ListDialog<DefaultImageTextSubtextDateItem> dialog = new ListDialog<>(this);
            dialog.setItems(items, ImageTextSubtextDateRow::new);
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            dialog.show();
        });
    }
}
