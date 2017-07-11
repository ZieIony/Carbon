package carbon.behavior;

import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;

import carbon.internal.MathUtils;

public class HeightBehavior<Type extends View> extends Behavior<Type> {

    private final float minHeight;
    private final float maxHeight;
    private final Direction direction;

    public enum Direction {
        Up, Down, Both
    }

    public HeightBehavior(Type target, float minHeight, float maxHeight, Direction direction) {
        super(target);
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.direction = direction;
    }

    @Override
    public PointF onScroll(float scrollX, float scrollY) {
        if (direction == Direction.Down && scrollY > 0 || direction == Direction.Up && scrollY < 0)
            return new PointF(scrollX, scrollY);
        int height = getTarget().getHeight();
        int newHeight = (int) MathUtils.constrain(height - scrollY, minHeight, maxHeight);
        setHeight(newHeight);
        float scrollLeft = 0;
        if (scrollY > 0) {
            scrollLeft = Math.max(0, scrollY - (height - newHeight));
        } else {
            scrollLeft = Math.max(0, -scrollY - (newHeight - height));
        }
        return new PointF(scrollX, scrollLeft);
    }

    private void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getTarget().getLayoutParams();
        if (layoutParams == null) {
            getTarget().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        } else {
            layoutParams.height = height;
            getTarget().setLayoutParams(layoutParams);
        }
    }
}
