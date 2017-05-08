package carbon.internal;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

public class ReplaceTransformationMethod implements TransformationMethod {

    private CharSequence charSequence;

    public ReplaceTransformationMethod(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    @Override
    public CharSequence getTransformation(CharSequence charSequence, View view) {
        return this.charSequence;
    }

    @Override
    public void onFocusChanged(View view, CharSequence charSequence, boolean b, int i, Rect rect) {

    }
}
