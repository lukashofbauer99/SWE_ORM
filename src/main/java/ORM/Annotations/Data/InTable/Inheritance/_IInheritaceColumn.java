package ORM.Annotations.Data.InTable.Inheritance;

import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;

public interface _IInheritaceColumn {
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

    void init(Class sc, IConverter converter, ORM orm) throws Exception;

}
