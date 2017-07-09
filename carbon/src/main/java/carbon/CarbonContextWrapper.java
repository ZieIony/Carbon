package carbon;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.view.LayoutInflater;

public class CarbonContextWrapper extends ContextWrapper {
    private LayoutInflater inflater;
    private CarbonResources resources;

    public static Context wrap(Context context) {
        if (context instanceof CarbonContextWrapper || context instanceof CarbonContextThemeWrapper)
            return context;
        return new CarbonContextWrapper(context);
    }

    private CarbonContextWrapper(Context base) {
        super(base);
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
