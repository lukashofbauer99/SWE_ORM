package ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Savers.IRelationalColumnSaver;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManyToManyColumnSaver implements IRelationalColumnSaver {


    @Override
    public Object preInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) {

        return null;
    }

    @Override
    public boolean postInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection) {
        List<Object> returnIDs = null;
        try {
            Field relationField = t.getSClass().getDeclaredField(relationalColumn.getName());
            relationField.setAccessible(true);
            if (relationField.get(entity) != null) {
                if (!((Collection) relationField.get(entity)).isEmpty()) {
                    returnIDs = new ArrayList<>();
                    for (Object i : (Collection) relationField.get(entity)) {
                        returnIDs.add(tableSaver.save(orm.getTables().get(i.getClass()), i, orm, connection));
                    }


                    List<String> nameOrder = new ArrayList<>();
                    nameOrder.add(t.getTableName());
                    nameOrder.add(orm.getTables().get(relationalColumn.getType()).getTableName());
                    java.util.Collections.sort(nameOrder);

                    String queryString = "";
                    Field idField = null;
                    Object entityID = null;
                    idField = t.getSClass().getDeclaredField(t.getId().getName());
                    idField.setAccessible(true);
                    entityID = orm.getConverter().JavaToDBValue(idField.get(entity));
                    queryString = "DELETE FROM " + nameOrder.get(0) + "_" + nameOrder.get(1) + " WHERE " + t.getTableName() + "ID = " + entityID+";";


                    for (Object i : returnIDs) {
                        queryString += "\nINSERT INTO " + nameOrder.get(0) + "_" + nameOrder.get(1) + " (" + t.getTableName() + "ID," + orm.getTables().get(relationalColumn.getType()).getTableName() + "ID)" +
                                " VALUES(" + entityID + "," + orm.getConverter().JavaToDBValue(i) + ");";
                    }

                    try (Statement stmt = connection.createStatement()) {
                            stmt.execute(queryString);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }
}
