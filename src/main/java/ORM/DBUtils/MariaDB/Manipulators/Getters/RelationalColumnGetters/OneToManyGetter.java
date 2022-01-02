package ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Getters.IRelationalColumnGetter;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class OneToManyGetter implements IRelationalColumnGetter {
    @Override
    public Object get(Class c, Object id, ORM orm, Connection connection, _IRelationalColumn relcol) {
        _ITable table = orm.getTables().get(c);
        _ITable reltable= orm.getTables().get(relcol.getType());
        Collection<Object> idCollection= new ArrayList<>();
        String getIDsString="";
        boolean hasRelations=false;

        getIDsString = "SELECT "+ reltable.getId().getColumnName();

        getIDsString +=" FROM " + reltable.getTableName();

        getIDsString += "\nWHERE " + relcol.getName()+"_"+table.getTableName() + " = " + orm.getConverter().JavaToDBValue(id) + ";";

        try (Statement stmt = connection.createStatement()) {
            if (getIDsString != "") {

                ResultSet resultSet = stmt.executeQuery(getIDsString);

                while (resultSet.next())
                {
                    hasRelations=true;
                    idCollection.add(orm.getConverter().DBToJavaValue(resultSet.getObject(1),relcol.getType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collection<Object> entityCollection = new ArrayList<>();
        if(hasRelations) {
            for (Object i : idCollection) {
                entityCollection.add(orm.get(relcol.getType(), i));
            }
        }
        return entityCollection;
    }
}
