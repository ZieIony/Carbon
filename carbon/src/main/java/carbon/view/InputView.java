package carbon.view;

import androidx.annotation.NonNull;
import carbon.widget.OnValidateListener;

/**
 * Interface of a view with input, which can be validated. Used by {@link
 * carbon.widget.InputLayout}
 */
public interface InputView extends ValidStateView {
    /**
     * Performs validation
     */
    void validate();

    /**
     * Adds a listener
     *
     * @param listener cannot be null
     */
    void addOnValidateListener(@NonNull OnValidateListener listener);

    /**
     * Removes a listener
     *
     * @param listener cannot be null
     */
    void removeOnValidateListener(@NonNull OnValidateListener listener);

    /**
     * Removes all listeners
     */
    void clearOnValidateListeners();
}
