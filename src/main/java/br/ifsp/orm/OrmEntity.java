package br.ifsp.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrmEntity {
    enum SGBD {
        POSTGRES,
        SQLITE,
    }

    SGBD value() default SGBD.SQLITE;
}
