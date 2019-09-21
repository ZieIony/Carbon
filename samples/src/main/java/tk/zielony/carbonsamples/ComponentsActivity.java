package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.component.AvatarTextListItemActivity;
import tk.zielony.carbonsamples.component.AvatarTextRatingSubtextDateListItemActivity;
import tk.zielony.carbonsamples.component.IconTextListItemActivity;
import tk.zielony.carbonsamples.component.ImageTextSubtextDateListItemActivity;
import tk.zielony.carbonsamples.component.RegisterActivity;

@ActivityAnnotation(title = R.string.componentsActivity_title)
public class ComponentsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Lists and forms composed of reusable components with data binding",
                new SampleActivityItem(IconTextListItemActivity.class),
                new SampleActivityItem(AvatarTextListItemActivity.class),
                new SampleActivityItem(ImageTextSubtextDateListItemActivity.class),
                new SampleActivityItem(AvatarTextRatingSubtextDateListItemActivity.class),
                new SampleActivityItem(RegisterActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
