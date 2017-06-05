package carbon;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

public class CarbonContextWrapper extends ContextWrapper {
    private CarbonLayoutInflater mInflater;
    private CarbonResources resources;

    public CarbonContextWrapper(Context base) {
        super(base);
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
