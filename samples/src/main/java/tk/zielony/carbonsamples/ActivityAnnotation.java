package tk.zielony.carbonsamples;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityAnnotation {
    int title() default 0;

    int layout() default 0;
}
