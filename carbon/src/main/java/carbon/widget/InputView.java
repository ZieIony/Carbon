package carbon.widget;

public interface InputView extends ValidStateView {
    void validate();

    void addOnValidateListener(OnValidateListener listener);

    void removeOnValidateListener(OnValidateListener listener);

    void clearOnValidateListeners();
}
