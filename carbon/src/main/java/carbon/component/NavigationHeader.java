package carbon.component;

import android.view.ViewGroup;

import carbon.R;

public class NavigationHeader extends DataBindingComponent<NavigationHeaderItem> {
    public NavigationHeader(ViewGroup parent) {
        super(parent, R.layout.carbon_navigation_header);
    }
}
