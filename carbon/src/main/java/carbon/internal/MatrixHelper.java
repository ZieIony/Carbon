package carbon.internal;

import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Marcin on 2015-01-15.
 */
public class MatrixHelper {
    private MatrixHelper() {
    }

    final static Matrix matrix = new Matrix();

    public static Matrix getMatrix(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return view.getMatrix();
        Animation animation = view.getAnimation();
        if (animation == null)
            return matrix;
        Transformation transformation = new Transformation();
        animation.getTransformation(view.getDrawingTime(), transformation);
        return transformation.getMatrix();
    }
}
