package com.tinkoff.timetable.annotation.validation.extraInfo;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Constraint(validatedBy = ExtraInfoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExtraInfo {

    String message() default "extra_info must match the pattern. Check swagger";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
