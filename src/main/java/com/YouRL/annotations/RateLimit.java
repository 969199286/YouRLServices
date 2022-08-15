package com.YouRL.annotations;

import com.YouRL.enums.LimitMethod;
import com.YouRL.enums.LimitType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface RateLimit {
    String key() default "RateLimit";

    String prefix() default "Annotation";

    LimitMethod limitMethod() default LimitMethod.PERMITS_BUCKET;

    double permitsPerSecond() default 1.0;

    int period() default 1;

    int permits() default 1;

    LimitType limitType() default LimitType.IP;
}
