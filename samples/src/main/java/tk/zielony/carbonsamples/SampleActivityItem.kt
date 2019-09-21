package tk.zielony.carbonsamples

import android.app.Activity
import java.io.Serializable

open class SampleActivityGroup : Serializable {
    var activityClass: Class<out Activity>? = null
        private set
    var name: Int? = null
        private set

    @JvmOverloads
    constructor(activityClass: Class<out Activity>) : super(
        ) {
          this.activityClass = activityClass
          this.name = activityClass.getAnnotation(ActivityAnnotation::class.java).title
    }
}

class SampleActivityItem : SampleActivityGroup {
    var isBeta = false
        private set
    var isStarred: Boolean = false

  @JvmOverloads
    constructor(activityClass: Class<out Activity>, beta:Boolean = false) : super(activityClass) {
      this.isBeta = beta
    }
}
