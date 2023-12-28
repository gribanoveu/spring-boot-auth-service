package com.github.gribanoveu.cuddle.utils.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Evgeny Gribanov
 * @version 26.10.2023
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogResponse {
    String message() default "";
}
