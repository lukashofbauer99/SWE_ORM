package ORM.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {

    String columnName() default "";

    String columnType() default "";

    boolean nullable() default true;

    String onDelete() default "CASCADE";
    String onUpdate() default "CASCADE";

}
