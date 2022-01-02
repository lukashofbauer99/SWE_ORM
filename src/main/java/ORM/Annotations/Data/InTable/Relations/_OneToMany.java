package ORM.Annotations.Data.InTable.Relations;

import ORM.Annotations.OneToMany;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

@Data
public class _OneToMany implements _IRelationalColumn {

    private final Class annotationClass = OneToMany.class;
    private String name;
    private String columnType;
    private String columnName;
    private Class type;
    private Boolean nullable;
    private String ColumnQuery;
    private String onDelete;
    private String onUpdate;


    @Override
    public void init(Field f, IConverter converter, ORM orm) throws Exception {
        OneToMany columnAnnotation = f.getAnnotation(OneToMany.class);

        name = f.getName();
        type = (Class<?>) (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);

        nullable = columnAnnotation.nullable();
        onDelete = columnAnnotation.onDelete();
        onUpdate = columnAnnotation.onUpdate();


        if (columnAnnotation.columnName().equals("") || columnAnnotation.columnName().equals(null))
            columnName = name;
        else
            columnName = columnAnnotation.columnName();

        String idType = orm.getTables().get(type).getId().getColumnType();

        if (idType == null)
            throw new Exception("Referenced class needs field annotated with @ID");

        if (columnAnnotation.columnType().equals("") || columnAnnotation.columnType().equals(null))
            columnType = idType;
        else
            columnType = columnAnnotation.columnType();
    }
}
