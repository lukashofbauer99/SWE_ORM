package ORM.DBUtils.MariaDB.Manipulators.Deleters;

import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Deleters.IEntityDeleter;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

public class MariaDBEntityDeleter implements IEntityDeleter {

    @Override
    public void delete(Object o, ORM orm, Connection connection) {
        _ITable table = orm.getTables().get(o.getClass());
        Field mainObjectIDField = null;
        Object mainObjectID = null;
        try {
            mainObjectIDField = table.getSClass().getDeclaredField(table.getId().getName());
            mainObjectIDField.setAccessible(true);
            mainObjectID = mainObjectIDField.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mainObjectID != null) {
            String deleteString = "DELETE FROM " + table.getTableName() + " WHERE " + table.getId().getColumnName() + " = " + orm.getConverter().JavaToDBValue(mainObjectID) + ";";

            try (Statement stmt = connection.createStatement()) {
                    stmt.execute(deleteString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
