package carbon.component;

/**
 * Created by Marcin on 2017-02-11.
 */

public class DefaultSubheaderItem implements SubheaderItem {

    private String text;

    public DefaultSubheaderItem(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
