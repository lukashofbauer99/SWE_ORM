package ORM.Annotations.Data;

import ORM.Annotations.Data.InTable.Inheritance._IInheritaceColumn;
import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data.InTable._ID;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.ORM;

import java.util.List;

public interface _ITable {

    Class getSClass();
    String getTableName();
    List<_IColumn> getColumns();
    List<_IRelationalColumn> getRelationalColumns();
    _IInheritaceColumn getInheritanceColumn();
    _ID getId();

    //Init methods
    void basicInit(Class c, IConverter converter, ORM orm) throws Exception;
    void inheritanceInit(Class c, IConverter converter, ORM orm);
    void relationalInit(Class c, IConverter converter, ORM orm);
}

