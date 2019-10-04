package carbon.internal;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.databinding.BindingAdapter;

import carbon.view.TintedView;
import carbon.widget.ImageView;

public class DataBinding {
    @BindingAdapter("srcCompat")
    public static void setDrawable(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }


    @BindingAdapter("carbon_tint")
    public static void setTint(View view, ColorStateList tint) {
        ((TintedView) view).setTintList(tint);
    }

    @BindingAdapter("carbon_tintMode")
    public static void setTintMode(View view, int mode) {
        ((TintedView) view).setTintMode(PorterDuff.Mode.values()[mode]);
    }

    @BindingAdapter("carbon_backgroundTint")
    public static void setBackgroundTint(View view, ColorStateList tint) {
        ((TintedView) view).setBackgroundTintList(tint);
    }

    @BindingAdapter("carbon_backgroundTintMode")
    public static void setBackgroundTintMode(View view, int mode) {
        ((TintedView) view).setBackgroundTintMode(PorterDuff.Mode.values()[mode]);
    }

    @BindingAdapter("carbon_animateColorChanges")
    public static void setAnimateColorChangesEnabled(View view, boolean enabled) {
        ((TintedView) view).setAnimateColorChangesEnabled(enabled);
    }
}
