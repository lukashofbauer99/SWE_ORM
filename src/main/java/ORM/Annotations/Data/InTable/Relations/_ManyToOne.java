package ORM.Annotations.Data.InTable.Relations;

import ORM.Annotations.ManyToOne;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class _ManyToOne implements _IRelationalColumn {

    private final Class annotationClass = ManyToOne.class;
    private String name;
    private String columnType;
    private String columnName;
    private Class type;
    private Boolean nullable;
    private String onDelete;
    private String onUpdate;


    @Override
    public void init(Field f, IConverter converter, ORM orm) throws Exception {
        ManyToOne columnAnnotation = f.getAnnotation(ManyToOne.class);

        name = f.getName();
        type = f.getType();
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
