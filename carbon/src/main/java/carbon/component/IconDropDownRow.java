package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.widget.DropDown;

public class IconDropDownRow<Type extends IconDropDownItem> extends DataBindingComponent<Type> {

    private DropDown dropDown;

    public IconDropDownRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_icondropdown);
        dropDown = getView().findViewById(R.id.carbon_dropDown);
    }

    @Override
    public void bind(Type data) {
        super.bind(data);
        dropDown.getAdapter().setItems(data.getItems());
    }

    public DropDown getDropDown() {
        return dropDown;
    }

    public Object getSelectedItem() {
        return dropDown.getSelectedItem();
    }

    public void setSelectedItem(Object item) {
        dropDown.setSelectedItem(item);
    }
}
