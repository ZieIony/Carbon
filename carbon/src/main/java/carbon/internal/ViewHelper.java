package carbon.internal;

import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Marcin on 2015-01-15.
 */
public class ViewHelper {
    private ViewHelper() {

    }

    public static Matrix getMatrix(View view) {
        Animation animation = view.getAnimation();
        if (animation == null)
            return new Matrix();
        Transformation transformation = new Transformation();
        animation.getTransformation(view.getDrawingTime(),transformation);
        return transformation.getMatrix();
    }
}
