package tk.zielony.carbonsamples

import android.app.Activity
import java.io.Serializable

open class SampleActivityGroup : Serializable {
    var activityClass: Class<out Activity>? = null
        private set
    var name: Int? = null
        private set
    var icon = 0
        private set

    @JvmOverloads
    constructor(activityClass: Class<out Activity>) : super(
    ) {
        this.activityClass = activityClass
        activityClass.getAnnotation(SampleAnnotation::class.java)?.let {
            this.name = it.titleId
            this.icon = it.iconId
        }
    }
}

class SampleActivityItem : SampleActivityGroup {
    var isBeta = false
        private set
    var isStarred: Boolean = false

    @JvmOverloads
    constructor(activityClass: Class<out Activity>, beta: Boolean = false) : super(activityClass) {
        this.isBeta = beta
    }
}
