package tk.zielony.carbonsamples;

/**
 * Created by Marcin on 2015-04-28.
 */
public class ViewModel {
    Class klass;
    String name;
    boolean beta = false, lollipop = false;

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
}
