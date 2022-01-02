package ORM.Annotations.Data;

import ORM.Annotations.Data.InTable.Inheritance._IInheritaceColumn;
import ORM.Annotations.Data.InTable.Inheritance._InheritanceOneToOne;
import ORM.Annotations.Data.InTable.Relations.*;
import ORM.Annotations.Data.InTable._Column;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data.InTable._ID;
import ORM.Annotations.ID;
import ORM.Annotations.Table;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
public class _BasicTable implements _ITable {

    private final List<_IColumn> supportedColumnDataClasses = new ArrayList<>();
    private final List<_IRelationalColumn> supportedRelationColumnDataClasses = new ArrayList<>();
    private final List<_IInheritaceColumn> supportedInheritaceColumnDataClasses = new ArrayList<>();

    private Class sClass = null;
    private String tableName = "";
    private List<_IColumn> columns = new ArrayList<>();
    private List<_IRelationalColumn> relationalColumns = new ArrayList<>();
    private _IInheritaceColumn inheritanceColumn = null;
    private _ID id = null;

    public _BasicTable() {

        initSupportedColumnDataClasses();
        initSupportedRelationalColumnDataClasses();
        initSupportedInheritanceColumnDataClasses();
    }

    private void initSupportedColumnDataClasses() {
        supportedColumnDataClasses.add(new _Column());
    }
    private void initSupportedRelationalColumnDataClasses() {
        supportedRelationColumnDataClasses.add(new _OneToOne());
        supportedRelationColumnDataClasses.add(new _ManyToOne());
        supportedRelationColumnDataClasses.add(new _ManyToMany());
        supportedRelationColumnDataClasses.add(new _OneToMany());
    }
    private void initSupportedInheritanceColumnDataClasses() {
        supportedInheritaceColumnDataClasses.add(new _InheritanceOneToOne());
    }

    public void basicInit(Class c, IConverter converter, ORM orm) throws Exception {
        sClass = c;
        Table tableAnnotation = (Table) c.getAnnotation(Table.class);
        if (tableAnnotation.name() == "") {
            tableName = c.getName();
        } else {
            tableName = tableAnnotation.name();
        }


        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(ID.class)) {
                id = new _ID();
                id.init(field, converter);
            }
            for (_IColumn column : supportedColumnDataClasses) {
                if (field.isAnnotationPresent(column.getAnnotationClass())) {
                    _IColumn tableColumn = null;
                    try {
                        tableColumn = column.getClass().getConstructor().newInstance();
                        tableColumn.init(field, converter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    columns.add(tableColumn);
                }
            }
        }

        if(id==null)
        {
            throw new Exception("Every Class Annotated with table needs a @ID");
        }
    }
    @SneakyThrows
    public void inheritanceInit(Class c, IConverter converter, ORM orm) {

        if (c.getSuperclass() != null) {
            for (_IInheritaceColumn column : supportedInheritaceColumnDataClasses) {
                if (c.isAnnotationPresent(column.getAnnotationClass())) {
                    _IInheritaceColumn tableColumn = null;
                    try {
                        tableColumn = column.getClass().getConstructor().newInstance();
                        tableColumn.init(c, converter, orm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    inheritanceColumn = tableColumn;
                }
            }
        }
    }
    public void relationalInit(Class c, IConverter converter, ORM orm) {
        for (Field field : c.getDeclaredFields()) {
            for (_IRelationalColumn column : supportedRelationColumnDataClasses) {
                if (field.isAnnotationPresent(column.getAnnotationClass())) {
                    _IRelationalColumn tableColumn = null;
                    try {
                        tableColumn = column.getClass().getConstructor().newInstance();
                        tableColumn.init(field, converter, orm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    relationalColumns.add(tableColumn);
                }
            }
        }
    }



}
