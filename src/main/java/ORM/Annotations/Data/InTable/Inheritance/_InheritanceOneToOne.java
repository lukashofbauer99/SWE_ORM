package ORM.Annotations.Data.InTable.Inheritance;

import ORM.Annotations.InheritOneToOne;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;
import lombok.Data;

@Data
public class _InheritanceOneToOne implements _IInheritaceColumn {

    private final Class annotationClass = InheritOneToOne.class;
    private String name;
    private String columnType;
    private String columnName;
    private Class type;
    private Boolean nullable;


    @Override
    public void init(Class initialClass, IConverter converter, ORM orm) throws Exception {
        type = initialClass.getSuperclass();
        nullable = false;
        name = "inheritance_col_"+ initialClass.getSuperclass().getSimpleName();

        String idType = orm.getTables().get(initialClass.getSuperclass()).getId().getColumnType();
        if (idType == null) {
            throw new Exception("class that is inherited from needs id Field");
        }
        columnType = idType;
        columnName = name;
    }
}
