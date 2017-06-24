package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import carbon.dialog.MultiSelectDialog;
import carbon.dialog.SingleSelectDialog;
import carbon.widget.DropDown;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.food.StringFruitGenerator;

public class SelectDialogActivity extends SamplesActivity {

    private String selectedItem;
    private List<String> selectedItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdialog);

        Samples.initToolbar(this, getString(R.string.simpleDialogActivity_title));

        EditText titleText = findViewById(R.id.titleText);
        DropDown<String> dropDown = findViewById(R.id.dropDown);
        dropDown.setItems(new String[]{"Single select", "Multi select"});

        StringFruitGenerator generator = new StringFruitGenerator();
        List<String> items = Stream.generate(generator::next).limit(5).toList();
        selectedItem = items.get(0);
        selectedItems = new ArrayList<>();
        selectedItems.add(selectedItem);

        findViewById(R.id.button).setOnClickListener(view -> {
            switch (dropDown.getSelectedIndex()) {
                case 0: {
                    SingleSelectDialog<String> dialog = new SingleSelectDialog<>(this);
                    if (titleText.length() > 0)
                        dialog.setTitle(titleText.getText());
                    dialog.setItems(items);
                    dialog.setOnDismissListener(dialogInterface -> selectedItem = dialog.getSelectedItem());
                    dialog.setSelectedItem(selectedItem);
                    dialog.show();
                }
                break;
                case 1: {
                    MultiSelectDialog<String> dialog = new MultiSelectDialog<>(this);
                    if (titleText.length() > 0)
                        dialog.setTitle(titleText.getText());
                    dialog.setItems(items);
                    dialog.setOnDismissListener(dialogInterface -> selectedItems = dialog.getSelectedItems());
                    dialog.setSelectedItems(selectedItems);
                    dialog.show();
                }
                break;
            }
        });
    }
}
