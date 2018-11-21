package tk.zielony.carbonsamples

import android.app.Activity
import java.io.Serializable

open class SampleActivityGroup : Serializable {
    var activityClass: Class<out Activity>? = null
        private set
    var name: String? = null
        private set

    constructor(activityClass: Class<out Activity>, name: String) {
        this.activityClass = activityClass
        this.name = name
    }
}

class SampleActivityItem : SampleActivityGroup {
    var isBeta = false
        private set
    var isStarred: Boolean = false

    constructor(activityClass: Class<out Activity>, name: String) : super(activityClass, name) {
    }

    constructor(activityClass: Class<out Activity>, name: String, beta: Boolean) : super(activityClass, name) {
        this.isBeta = beta
    }
}
