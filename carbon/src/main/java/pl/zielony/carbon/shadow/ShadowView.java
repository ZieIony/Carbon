package pl.zielony.carbon.shadow;

/**
 * Created by Marcin on 2014-11-19.
 */
public interface ShadowView {
    float getElevation();

    void setElevation(float elevation);

    float getTranslationZ();

    void setTranslationZ(float translationZ);

    boolean isRect();

    void setRect(boolean rect);
}
