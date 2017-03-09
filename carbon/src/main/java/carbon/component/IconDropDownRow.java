package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;
import carbon.widget.DropDown;

public class IconDropDownRow extends DataBindingComponent<IconDropDownItem> {
    public static final RowFactory FACTORY = IconDropDownRow::new;

    private DropDown dropDown;

    public IconDropDownRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_icondropdown);
        dropDown = (DropDown) getView().findViewById(R.id.carbon_dropDown);
    }

    @Override
    public void bind(IconDropDownItem data) {
        super.bind(data);
        dropDown.getAdapter().setItems(data.getItems());
    }

    public DropDown getDropDown() {
        return dropDown;
    }

    public Object getSelectedItem(){
        return dropDown.getSelectedItem();
    }

    public void setSelectedItem(Object item){
        dropDown.setSelectedItem(item);
    }
}
