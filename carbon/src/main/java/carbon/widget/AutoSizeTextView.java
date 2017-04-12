package carbon.widget;

import android.support.annotation.NonNull;

/**
 * Interface of a text view capable of automatic text size adjusting.
 */
public interface AutoSizeTextView {

    /**
     * @return current auto text size mode
     */
    @NonNull
    AutoSizeTextMode getAutoSizeText();

    /**
     * @param autoSizeText new text size mode
     */
    void setAutoSizeText(@NonNull AutoSizeTextMode autoSizeText);

    /**
     * Gets minimum text size the view allows
     *
     * @return minimum text size
     */
    float getMinTextSize();

    /**
     * Sets minimum text size the view allows
     *
     * @param minTextSize new minimum text size
     */
    void setMinTextSize(float minTextSize);

    /**
     * Gets maximum text size the view allows
     *
     * @return maximum text size
     */
    float getMaxTextSize();

    /**
     * Sets maximum text size the view allows
     *
     * @param maxTextSize maximum text size
     */
    void setMaxTextSize(float maxTextSize);

    /**
     * Gets automatic text size granularity.
     *
     * @return granularity
     */
    float getAutoSizeStepGranularity();

    /**
     * Sets automatic text size granularity. Text can only take values which are equal to minSize *
     * [0 to N] * granularity or maxSize. This parameter helps to keep automatic text sizes count
     * low and easy to predict. Setting granularity to a larger value speeds up the adjustment
     * process.
     *
     * @param autoSizeStepGranularity granularity
     */
    void setAutoSizeStepGranularity(float autoSizeStepGranularity);
}
