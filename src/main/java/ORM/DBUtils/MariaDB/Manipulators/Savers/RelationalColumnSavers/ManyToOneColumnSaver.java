package ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Savers.IRelationalColumnSaver;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;

public class ManyToOneColumnSaver implements IRelationalColumnSaver {
    @Override
    public Object preInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) {
        Object returnID = null;
            try {
                Field relationField = t.getSClass().getDeclaredField(relationalColumn.getName());
                relationField.setAccessible(true);
                if(relationField.get(entity)!=null)
                    returnID= tableSaver.save(orm.getTables().get(relationField.get(entity).getClass()), relationField.get(entity), orm,connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return returnID;
    }


    @Override
    public boolean postInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) {
        return true;
    }
}
