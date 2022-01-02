package ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Savers.IRelationalColumnSaver;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;

public class OneToManyColumnSaver implements IRelationalColumnSaver {
    @Override
    public Object preInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) {
        return null;
    }


    @Override
    public boolean postInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) { //Alle Inserten updaten in der Collection
        Field relationalField = null;
        try {
            relationalField = t.getSClass().getDeclaredField(relationalColumn.getName());
            relationalField.setAccessible(true);

            Field iDField = t.getSClass().getDeclaredField(t.getId().getName());
            iDField.setAccessible(true);
            Object id = iDField.get(entity);
            Object relationList=relationalField.get(entity);
           if(relationList!=null) {
               if(!((Collection)relationList).isEmpty()) {
                   for (Object i : (Iterable) relationList) {
                       String relationObjectString = tableSaver.saveWithAdditionalRelation(orm.getTables().get(i.getClass()), i, orm, connection, relationalColumn.getColumnName() + "_" + t.getTableName(), id) + "\n";
                   }
               }
           }
        } catch (Exception e) {
           return false;
        }
        return true;
    }
}
