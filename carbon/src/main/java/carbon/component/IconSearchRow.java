package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonRowIconsearchBinding;
import carbon.widget.SearchEditText;

public class IconSearchRow extends DataBindingComponent<IconSearchItem> {

    public IconSearchRow(ViewGroup parent, SearchEditText.SearchDataProvider dataProvider, SearchEditText.OnFilterListener listener) {
        super(parent, R.layout.carbon_row_iconsearch);
        ((CarbonRowIconsearchBinding) getBinding()).carbonQuery.setDataProvider(dataProvider);
        ((CarbonRowIconsearchBinding) getBinding()).carbonQuery.setOnFilterListener(listener);
    }

}
