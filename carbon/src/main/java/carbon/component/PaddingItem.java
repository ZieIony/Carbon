package carbon.component;

import java.io.Serializable;

public class PaddingItem implements Serializable {

    private int padding;

    public PaddingItem(int padding) {
        this.padding = padding;
    }

    public int getPadding() {
        return padding;
    }
}
