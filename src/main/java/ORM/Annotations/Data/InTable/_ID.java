package ORM.Annotations.Data.InTable;

import ORM.Annotations.ID;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class _ID implements _IIDColumn {

    private final Class annotationClass = ID.class;
    private String name = "";
    private String columnType = "";
    private String columnName = "";
    private Class type = null;
    private Boolean nullable = false;
    private Boolean autoIncrement = true;

    @Override
    public void init(Field f, IConverter converter) {
        ID columnAnnotation = f.getAnnotation(ID.class);

        name = f.getName();
        type = f.getType();
        autoIncrement = columnAnnotation.autoIncrement();

        if (columnAnnotation.columnName().equals("") || columnAnnotation.columnName().equals(null))
            columnName = name;
        else
            columnName = columnAnnotation.columnName();

        if (columnAnnotation.columnType().equals("") || columnAnnotation.columnType().equals(null))
            columnType = converter.JavaToDBType(f.getType());
        else
            columnType = columnAnnotation.columnName();
    }
}
