package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.component.AvatarTextListItemActivity;
import tk.zielony.carbonsamples.component.IconTextListItemActivity;
import tk.zielony.carbonsamples.component.ImageTextSubtextDateListItemActivity;
import tk.zielony.carbonsamples.component.RegisterActivity;


public class ComponentsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.animationsActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Lists and forms composed of reusable components with data binding",
                new SampleActivityItem(IconTextListItemActivity.class, getString(R.string.iconTextListItemActivity_title)),
                new SampleActivityItem(AvatarTextListItemActivity.class, getString(R.string.avatarTextListItemActivity_title)),
                new SampleActivityItem(ImageTextSubtextDateListItemActivity.class, getString(R.string.imageTextSubtextDateListItemActivity_title)),
                new SampleActivityItem(RegisterActivity.class, getString(R.string.registerActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
