package tk.zielony.carbonsamples;

import android.os.Bundle;

public class ViewModel {
    Class klass;
    String name;
    boolean beta = false, lollipop = false;
    Bundle extras;

    public ViewModel(Class klass, String name) {
        this.klass = klass;
        this.name = name;
    }

    public ViewModel(Class klass, String name, boolean beta) {
        this.klass = klass;
        this.name = name;
        this.beta = beta;
    }

    public ViewModel(Class klass, String name, boolean beta, boolean lollipop) {
        this.klass = klass;
        this.name = name;
        this.beta = beta;
        this.lollipop = lollipop;
    }

    ViewModel withExtras(Bundle extras) {
        this.extras = extras;
        return this;
    }
}
