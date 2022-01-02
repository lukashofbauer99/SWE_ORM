package ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Getters.IRelationalColumnGetter;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ManyToManyGetter implements IRelationalColumnGetter {

    List<Object> loadedObjectIds= new ArrayList<>();

    @Override
    public Object get(Class c, Object id, ORM orm, Connection connection, _IRelationalColumn relcol) {
        Collection<Object> entityCollection = new ArrayList<>();
        if (!loadedObjectIds.contains(id)) {
            loadedObjectIds.add(id);
            _ITable table = orm.getTables().get(c);
            _ITable reltable = orm.getTables().get(relcol.getType());
            Collection<Object> idCollection = new ArrayList<>();
            String getIDsString = "";
            boolean hasRelations = false;

            List<String> nameOrder = new ArrayList<>();
            nameOrder.add(table.getTableName());
            nameOrder.add(reltable.getTableName());
            java.util.Collections.sort(nameOrder);

            getIDsString = "SELECT " + reltable.getTableName() + "ID ";

            getIDsString += " FROM " + nameOrder.get(0) + "_" + nameOrder.get(1);

            getIDsString += "\nWHERE " + table.getTableName() + "ID = " + orm.getConverter().JavaToDBValue(id) + ";";

            try (Statement stmt = connection.createStatement()) {
                if (getIDsString != "") {

                    ResultSet resultSet = stmt.executeQuery(getIDsString);

                    while (resultSet.next()) {
                        hasRelations = true;
                        idCollection.add(orm.getConverter().DBToJavaValue(resultSet.getObject(1), relcol.getType()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (hasRelations) {
                for (Object i : idCollection) {
                    entityCollection.add(orm.get(relcol.getType(), i));
                }
            }
            loadedObjectIds.remove(id);
        }
        return entityCollection;
    }
}
