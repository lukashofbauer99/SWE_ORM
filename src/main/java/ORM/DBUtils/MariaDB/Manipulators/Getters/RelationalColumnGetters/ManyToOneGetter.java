package ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Getters.IRelationalColumnGetter;
import ORM.ORM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class ManyToOneGetter implements IRelationalColumnGetter {
    @Override
    public Object get(Class c, Object id, ORM orm, Connection connection, _IRelationalColumn relcol) {
        _ITable table = orm.getTables().get(c);
        _ITable reltable= orm.getTables().get(relcol.getType());
        Object relId=null;
        String getIDsString="";
        boolean hasRelations= false;

        getIDsString = "SELECT "+ relcol.getColumnName();

        getIDsString +=" FROM " + table.getTableName();

        getIDsString += "\nWHERE " + table.getId().getColumnName() + " = " + orm.getConverter().JavaToDBValue(id) + ";";

        try (Statement stmt = connection.createStatement()) {
            if (getIDsString != "") {

                ResultSet resultSet = stmt.executeQuery(getIDsString);
                hasRelations=resultSet.first();
                if(hasRelations) {
                    relId = orm.getConverter().DBToJavaValue(resultSet.getObject(1),relcol.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object relEntity = null;
        if(hasRelations)
            relEntity=orm.get(relcol.getType(),relId);
        return relEntity;
    }
}
