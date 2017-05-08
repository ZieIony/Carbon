package tk.zielony.carbonsamples;

import android.os.Bundle;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.dialog.ListDialogActivity;
import tk.zielony.carbonsamples.dialog.SelectDialogActivity;
import tk.zielony.carbonsamples.dialog.SimpleDialogActivity;

public class DialogsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.dialogsActivity_title));

        setItems(new Object[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Various dialog windows",
                new SampleActivityItem(SimpleDialogActivity.class, getString(R.string.simpleDialogActivity_title)),
                new SampleActivityItem(SelectDialogActivity.class, getString(R.string.selectDialogActivity_title)),
                new SampleActivityItem(ListDialogActivity.class, getString(R.string.listDialogActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }
}
