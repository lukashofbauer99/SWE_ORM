package ORM.Annotations.Data.InTable;

import ORM.DBUtils.General.Creators.Converters.IConverter;

import java.lang.reflect.Field;

public interface _IColumn {
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


    void init(Field f, IConverter converter) throws Exception;

}
