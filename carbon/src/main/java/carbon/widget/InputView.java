package carbon.widget;

/**
 * Created by Marcin on 2017-02-04.
 */

public interface InputView extends ValidStateView {
    void validate();

    void addOnValidateListener(OnValidateListener listener);

    void removeOnValidateListener(OnValidateListener listener);

    void clearOnValidateListeners();
}
