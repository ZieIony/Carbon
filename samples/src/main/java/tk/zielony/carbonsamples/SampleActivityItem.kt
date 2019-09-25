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
    constructor(activityClass: Class<out Activity>, icon: Int = 0) : super(
        ) {
        this.activityClass = activityClass
        this.name = activityClass.getAnnotation(ActivityAnnotation::class.java)?.title
        this.icon = icon
    }
}

class SampleActivityItem : SampleActivityGroup {
    var isBeta = false
        private set
    var isStarred: Boolean = false

    @JvmOverloads
    constructor(activityClass: Class<out Activity>, icon: Int = 0, beta: Boolean = false) : super(activityClass, icon) {
        this.isBeta = beta
    }
}
