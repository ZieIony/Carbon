package carbon.drawable;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Marcin on 2015-04-30.
 */
public class EdgeEffectCompat extends android.support.v4.widget.EdgeEffectCompat {
    EdgeEffect impl;
    private float displacement;

    /**
     * Construct a new EdgeEffect themed using the given context.
     * <p/>
     * <p>Note: On platform versions that do not support EdgeEffect, all operations
     * on the newly constructed object will be mocked/no-ops.</p>
     *
     * @param context Context to use for theming the effect
     */
    public EdgeEffectCompat(Context context) {
        super(context);
        impl = new EdgeEffect(context);
    }

    @Override
    public void setSize(int width, int height) {
        impl.setSize(width, height);
    }

    @Override
    public boolean isFinished() {
        return impl.isFinished();
    }

    @Override
    public void finish() {
        impl.finish();
    }

    @Override
    public boolean onPull(float deltaDistance) {
        impl.onPull(deltaDistance, displacement);
        return true;
    }

    @Override
    public boolean onPull(float deltaDistance, float displacement) {
        impl.onPull(deltaDistance, displacement);
        return true;
    }

    @Override
    public boolean onRelease() {
        impl.onRelease();
        return impl.isFinished();
    }

    @Override
    public boolean onAbsorb(int velocity) {
        impl.onAbsorb(velocity);
        return true;
    }

    @Override
    public boolean draw(Canvas canvas) {
        return impl.draw(canvas);
    }

    public void setDisplacement(float displacement) {
        this.displacement = displacement;
    }

    public void setColor(int color) {
        impl.setColor(color);
    }
}
