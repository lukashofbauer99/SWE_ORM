package ORM.DBUtils.MariaDB.Manipulators.Getters;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data._ITable;
import ORM.Annotations.ManyToMany;
import ORM.Annotations.ManyToOne;
import ORM.Annotations.OneToMany;
import ORM.Annotations.OneToOne;
import ORM.DBUtils.General.Manipulators.Getters.IEntityGetter;
import ORM.DBUtils.General.Manipulators.Getters.IRelationalColumnGetter;
import ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters.ManyToManyGetter;
import ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters.ManyToOneGetter;
import ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters.OneToManyGetter;
import ORM.DBUtils.MariaDB.Manipulators.Getters.RelationalColumnGetters.OneToOneGetter;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MariaDBEntityGetter implements IEntityGetter {

    Map<Class, IRelationalColumnGetter> relationalColumnGetters = new HashMap<>();

    public MariaDBEntityGetter() {
        initRelationalColumnGetters();
    }

    private void initRelationalColumnGetters() {
        relationalColumnGetters.put(OneToOne.class, new OneToOneGetter());
        relationalColumnGetters.put(OneToMany.class, new OneToManyGetter());
        relationalColumnGetters.put(ManyToMany.class, new ManyToManyGetter());
        relationalColumnGetters.put(ManyToOne.class, new ManyToOneGetter());
    }


    @Override
    public Object get(Class c, Object id, ORM orm, Connection connection) {
        boolean entityFound=false;
        Object returnObject = null;

        _ITable table = orm.getTables().get(c);
        String getString = "SELECT ";

        String columnString = "";
        for (_IColumn col : table.getColumns()) {
            if (columnString == "")
                columnString += "m." + col.getColumnName();
            else
                columnString += " ,m." + col.getColumnName();
        }
        if (table.getInheritanceColumn() != null) {
            for (_IColumn col : orm.getTables().get(table.getInheritanceColumn().getType()).getColumns()) {
                columnString += " ,i." + col.getColumnName();
            }
        }
        getString += columnString + " FROM " + table.getTableName() + " m";

        if (table.getInheritanceColumn() != null) {
            getString += " \nJOIN " + orm.getTables().get(table.getInheritanceColumn().getType()).getTableName() +
                    " i ON m." + table.getInheritanceColumn().getColumnName() + " = " + "i." + orm.getTables().get(table.getInheritanceColumn().getType()).getId().getColumnName();
        }

        getString += "\nWHERE m." + table.getId().getColumnName() + " = " + orm.getConverter().JavaToDBValue(id) + ";";

        try (Statement stmt = connection.createStatement()) {
            if (getString != "") {
                ResultSet resultSet = stmt.executeQuery(getString);
                entityFound=resultSet.first();
                if(entityFound) {
                returnObject = c.getConstructor().newInstance();
                Field idField = returnObject.getClass().getDeclaredField(table.getId().getColumnName());
                idField.setAccessible(true);
                idField.set(returnObject, id);

                    int resultSetColumnCounter = 1;
                    for (_IColumn col : table.getColumns()) {
                        Field field = returnObject.getClass().getDeclaredField(col.getColumnName());
                        field.setAccessible(true);
                        field.set(returnObject, orm.getConverter().DBToJavaValue(resultSet.getObject(resultSetColumnCounter), col.getType()));
                        resultSetColumnCounter++;
                    }
                    if (table.getInheritanceColumn() != null) {
                        for (_IColumn col : orm.getTables().get(table.getInheritanceColumn().getType()).getColumns()) {
                            Field field = returnObject.getClass().getSuperclass().getDeclaredField(col.getColumnName());
                            field.setAccessible(true);
                            field.set(returnObject, orm.getConverter().DBToJavaValue(resultSet.getObject(resultSetColumnCounter), col.getType()));
                            resultSetColumnCounter++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(entityFound) {
            for (_IRelationalColumn col : table.getRelationalColumns()) {
                Field field = null;
                try {
                    field = returnObject.getClass().getDeclaredField(col.getColumnName());
                    field.setAccessible(true);
                    field.set(returnObject, relationalColumnGetters.get(col.getAnnotationClass()).get(c, id, orm, connection, col));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnObject;
    }
}
