package carbon;

import android.content.Context;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;

public class CarbonContextThemeWrapper extends ContextThemeWrapper {
    private CarbonLayoutInflater mInflater;
    private CarbonResources resources;

    public CarbonContextThemeWrapper(Context base, int theme) {
        super(base, theme);
        resources = new CarbonResources(this, getAssets(), super.getResources().getDisplayMetrics(), super.getResources().getConfiguration());
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    /*@Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new CarbonLayoutInflater(LayoutInflater.from(getBaseContext()), this);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }*/
}
