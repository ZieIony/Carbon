package tk.zielony.carbonsamples

@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityAnnotation(
        val title: Int = 0,
        val layout: Int = 0,
        val menu: Int = 0,
        val icon: Int = 0
)
