package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;

import carbon.R;
import carbon.drawable.VectorDrawable;

public class DefaultIconSearchItem implements IconSearchItem {

    private Drawable icon;
    private String query = "", hint = "";

    public DefaultIconSearchItem(Context context) {
        icon = new VectorDrawable(context.getResources(), R.raw.carbon_search);
    }

    public DefaultIconSearchItem(Context context, String query, String hint) {
        icon = new VectorDrawable(context.getResources(), R.raw.carbon_search);
        this.query = query;
        this.hint = hint;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getHint() {
        return hint;
    }
}
