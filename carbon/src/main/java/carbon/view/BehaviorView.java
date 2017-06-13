package carbon.view;

import android.view.View;

import carbon.beta.Behavior;

public interface BehaviorView {
    void addBehavior(Behavior behavior);

    void removeBehavior(View view);
}
