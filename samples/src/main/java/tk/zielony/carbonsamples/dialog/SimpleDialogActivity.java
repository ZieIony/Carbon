package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import carbon.dialog.TextDialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_simpledialog, titleId = R.string.simpleDialogActivity_title)
public class SimpleDialogActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        EditText titleText = findViewById(R.id.titleText);
        EditText buttonText = findViewById(R.id.buttonText);

        findViewById(R.id.showDialog).setOnClickListener(view -> {
            TextDialog dialog = new TextDialog(this);
            dialog.setText("Danish carrot cake toffee cupcake caramels chocolate cheesecake. Sugar plum chocolate cake dragée chocolate cake chupa chups jelly dessert. Icing lemon drops ice cream. Sesame snaps jelly cake biscuit. Ice cream halvah cookie gingerbread cookie candy donut sweet.");
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            if (buttonText.length() > 0)
                dialog.addButton(buttonText.getText().toString(), null);
            dialog.show();
        });
    }
}
