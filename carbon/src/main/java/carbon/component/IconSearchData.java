package carbon.component;

import android.content.Context;
import android.graphics.drawable.Drawable;

import carbon.R;
import carbon.drawable.VectorDrawable;

/**
 * Created by Marcin on 2017-02-12.
 */
public class IconSearchData {

    private String query = "", hint = "";

    public IconSearchData() {
    }

    public IconSearchData(String query, String hint) {
        this.query = query;
        this.hint = hint;
    }

    public Drawable getIcon(Context context) {
        return new VectorDrawable(context.getResources(), R.raw.carbon_search);
    }

    public String getQuery() {
        return query;
    }

    public String getHint() {
        return hint;
    }
}
