package carbon;

import android.content.Context;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

public class CarbonContextThemeWrapper extends ContextThemeWrapper {
    private LayoutInflater inflater;
    private CarbonResources resources;

    public CarbonContextThemeWrapper(Context base, int theme) {
        super(base, theme);
        resources = new CarbonResources(this, getAssets(), super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }

}
