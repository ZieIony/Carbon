package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.dialog.ListDialogActivity;
import tk.zielony.carbonsamples.dialog.ProgressDialogActivity;
import tk.zielony.carbonsamples.dialog.SelectDialogActivity;
import tk.zielony.carbonsamples.dialog.SimpleDialogActivity;

@ActivityAnnotation(title = R.string.dialogsActivity_title, layout = R.layout.activity_samplelist)
public class DialogsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Various dialog windows",
                new SampleActivityItem(ListDialogActivity.class),
                new SampleActivityItem(ProgressDialogActivity.class),
                new SampleActivityItem(SelectDialogActivity.class),
                new SampleActivityItem(SimpleDialogActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }
}
