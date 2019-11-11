package carbon.view

import carbon.behavior.Behavior

interface BehaviorView {
    fun addBehavior(behavior: Behavior<*>)

    fun removeBehavior(behavior: Behavior<*>)
}
