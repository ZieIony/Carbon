package carbon.component;

public class DefaultCheckBoxItem implements CheckBoxItem {
    private boolean checked;
    private String label;

    public DefaultCheckBoxItem(boolean checked, String label) {
        this.checked = checked;
        this.label = label;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String getText() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
