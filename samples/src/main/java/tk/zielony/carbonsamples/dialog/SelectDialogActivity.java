package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.annimon.stream.Stream;

import carbon.dialog.SingleSelectDialog;
import carbon.widget.DropDown;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.food.StringFruitGenerator;

public class SelectDialogActivity extends SamplesActivity {

    private Object selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdialog);

        Samples.initToolbar(this, getString(R.string.simpleDialogActivity_title));

        EditText titleText = (EditText) findViewById(R.id.titleText);
        DropDown dropDown = (DropDown) findViewById(R.id.dropDown);
        dropDown.setItems(new String[]{"Single select"});

        StringFruitGenerator generator = new StringFruitGenerator();
        Object[] items = Stream.generate(generator::next).limit(5).toArray();
        selectedItem = items[0];

        findViewById(R.id.button).setOnClickListener(view -> {
            switch (dropDown.getSelectedIndex()) {
                case 0:
                    SingleSelectDialog dialog = new SingleSelectDialog(this);
                    if (titleText.length() > 0)
                        dialog.setTitle(titleText.getText());
                    dialog.setItems(items);
                    dialog.setOnItemClickedListener((view1, position) -> {
                        selectedItem = items[position];
                    });
                    dialog.setSelectedItem(selectedItem);
                    dialog.show();
                    break;
            }
        });
    }
}
