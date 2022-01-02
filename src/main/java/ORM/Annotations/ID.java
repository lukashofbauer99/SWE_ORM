package ORM.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ID {

    String columnName() default "";

    String columnType() default "";

    boolean autoIncrement() default true;
}
