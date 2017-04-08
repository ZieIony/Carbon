package carbon.component;

public class DefaultHeaderItem implements HeaderItem {

    private String text;

    public DefaultHeaderItem(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
