package carbon.shadow;

/**
 * Created by Marcin on 2014-11-19.
 */
public interface ShadowView {
    float getElevation();

    void setElevation(float elevation);

    float getTranslationZ();

    void setTranslationZ(float translationZ);

    ShadowShape getShadowShape();

    Shadow getShadow();

    void invalidateShadow();
}
