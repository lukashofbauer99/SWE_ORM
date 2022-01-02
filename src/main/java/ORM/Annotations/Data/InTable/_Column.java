package ORM.Annotations.Data.InTable;

import ORM.Annotations.Column;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class _Column implements _IColumn {

    private final Class annotationClass = Column.class;
    private String name = "";
    private String columnType = "";
    private String columnName = "";
    private Class type = null;
    private Boolean nullable;


    @Override
    public void init(Field f, IConverter converter) {
        Column columnAnnotation = f.getAnnotation(Column.class);

        name = f.getName();
        type = f.getType();
        nullable = columnAnnotation.nullable();

        if (columnAnnotation.columnName().equals("") || columnAnnotation.columnName().equals(null))
            columnName = name;
        else
            columnName = columnAnnotation.columnName();

        if (columnAnnotation.columnType().equals("") || columnAnnotation.columnType().equals(null))
            columnType = converter.JavaToDBType(f.getType());
        else
            columnType = columnAnnotation.columnType();

    }

}