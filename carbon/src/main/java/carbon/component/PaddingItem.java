package carbon.component;

public class PaddingItem implements ComponentItem {

    private int padding;

    public PaddingItem(int padding) {
        this.padding = padding;
    }

    public int getPadding() {
        return padding;
    }
}
