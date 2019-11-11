package tk.zielony.carbonsamples

@Retention(AnnotationRetention.RUNTIME)
annotation class SampleAnnotation(
        val titleId: Int = 0,
        val layoutId: Int = 0,
        val menuId: Int = 0,
        val iconId: Int = 0
)
