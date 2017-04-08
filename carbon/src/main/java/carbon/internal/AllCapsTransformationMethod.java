package carbon.internal;

import android.content.Context;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.View;

import java.util.Locale;

/**
 * Transformation method used for making all characters capitalized. Can be used by all classes
 * derived from TextView.
 *
 * @see(android.widget.TextView)
 */
public class AllCapsTransformationMethod implements TransformationMethod {
    private Locale locale;

    /**
     * Uses current locale.
     *
     * @param context Context to get locale from
     */
    public AllCapsTransformationMethod(Context context) {
        locale = context.getResources().getConfiguration().locale;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        if (source == null)
            return null;
        if (source instanceof Spanned) {
            SpannableString string = new SpannableString(source.toString().toUpperCase(locale));
            TextUtils.copySpansFrom((Spanned) source, 0, source.length(), null, string, 0);
            return string;
        }
        return source.toString().toUpperCase(locale);
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction,
                               Rect previouslyFocusedRect) {
    }
}
