package carbon.behavior;

import android.graphics.PointF;

import carbon.widget.RecyclerView;

public class RecyclerScrollBehavior extends Behavior<RecyclerView> {
    public RecyclerScrollBehavior(RecyclerView target) {
        super(target);
    }

    @Override
    public PointF onScroll(float scrollX, float scrollY) {
        int recyclerScrollY = getTarget().getListScrollY();
        int maxScrollY = getTarget().getMaxScrollY();
        float scrollLeft = 0;
        if (-scrollY > recyclerScrollY && scrollY < 0) {
            scrollLeft = scrollY + recyclerScrollY;
            scrollY -= scrollLeft;
        } else if (scrollY > maxScrollY - recyclerScrollY && scrollY > 0) {
            scrollLeft = recyclerScrollY - maxScrollY;
            scrollY -= scrollLeft;
        }
        getTarget().scrollBy((int) scrollX, (int) scrollY);
        return new PointF(0, scrollLeft);
    }
}
