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
    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
        if ((id & 0xffff0000) == 0x7f050000) {
            return new VectorDrawable(this, id);
        } else {
            return super.getDrawable(id, theme);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        if ((id & 0xffff0000) == 0x7f050000) {
            return new VectorDrawable(this, id);
        } else {
            return super.getDrawable(id);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        if ((id & 0xffff0000) == 0x7f050000) {
            return new VectorDrawable(this, id);
        } else {
            return super.getDrawableForDensity(id, density, theme);
        }
    }

    @Nullable
    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        if ((id & 0xffff0000) == 0x7f050000) {
            return new VectorDrawable(this, id);
        } else {
            return super.getDrawableForDensity(id, density);
        }
    }

}
