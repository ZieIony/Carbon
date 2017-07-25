package carbon.component;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class DefaultIconDropDownItem<Type extends Serializable> implements IconDropDownItem<Type> {
    private Drawable icon;
    private String hint;
    private Type[] items;
    private Type selectedItem;

    public DefaultIconDropDownItem(Drawable icon, String hint, Type[] items, Type selectedItem) {
        this.icon = icon;
        this.hint = hint;
        this.items = items;
        this.selectedItem = selectedItem;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public Type[] getItems() {
        return items;
    }

    public void setItems(Type[] items) {
        this.items = items;
    }

    @Override
    public Type getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Type selectedItem) {
        this.selectedItem = selectedItem;
    }
}
