package carbon;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import carbon.drawable.VectorDrawable;

/**
 * Created by Marcin on 2015-07-01.
 */
public class CarbonResources extends Resources {
    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     */
    public CarbonResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    @Nullable
    @Override
    public Drawable getDrawable(int resId, Theme theme) throws NotFoundException {
        if (getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawable(resId, theme);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawable(int resId) throws NotFoundException {
        if (getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawable(resId);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int resId, int density, Theme theme) {
        if (getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawableForDensity(resId, density, theme);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int resId, int density) throws NotFoundException {
        if (getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawableForDensity(resId, density);
        }
    }

}
