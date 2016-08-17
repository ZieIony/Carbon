package carbon.widget;

/**
 * Created by Marcin on 2016-07-28.
 */
public interface InputView {
    boolean isValid();

    void validate();

    void addOnValidateListener(OnValidateListener listener);

    void removeOnValidateListener(OnValidateListener listener);

    void clearOnValidateListeners();
}
