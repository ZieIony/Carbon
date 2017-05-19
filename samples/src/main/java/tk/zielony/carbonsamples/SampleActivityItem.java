package tk.zielony.carbonsamples;

import android.app.Activity;

import java.io.Serializable;

public class SampleActivityItem implements Serializable {
    private Class<? extends Activity> activityClass;
    private String name;
    private boolean beta = false;

    public SampleActivityItem(Class<? extends Activity> activityClass, String name) {
        this.activityClass = activityClass;
        this.name = name;
    }

    public SampleActivityItem(Class<? extends Activity> activityClass, String name, boolean beta) {
        this.activityClass = activityClass;
        this.name = name;
        this.beta = beta;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public String getName() {
        return name;
    }

    public boolean isBeta() {
        return beta;
    }
}
