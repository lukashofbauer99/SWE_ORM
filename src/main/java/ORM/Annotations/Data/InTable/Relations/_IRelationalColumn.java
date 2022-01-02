package ORM.Annotations.Data.InTable.Relations;

import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;

import java.lang.reflect.Field;

public interface _IRelationalColumn {
    Class getAnnotationClass();

    String getName();
    void setName(String name);

    String getColumnType();
    void setColumnType(String columnType);

    String getColumnName();
    void setColumnName(String columnName);

    Class getType();
    void setType(Class c);

    Boolean getNullable();
    void setNullable(Boolean nullable);

    String getOnDelete();
    void setOnDelete(String onDelete);

    String getOnUpdate();
    void setOnUpdate(String onUpdate);

    void init(Field f, IConverter converter, ORM orm) throws Exception;

}
