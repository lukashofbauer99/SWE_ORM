package ORM.DBUtils.MariaDB.Manipulators.Savers;

import ORM.Annotations.*;
import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Savers.IRelationalColumnSaver;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers.ManyToManyColumnSaver;
import ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers.ManyToOneColumnSaver;
import ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers.OneToManyColumnSaver;
import ORM.DBUtils.MariaDB.Manipulators.Savers.RelationalColumnSavers.OneToOneColumnSaver;
import ORM.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MariaDBEntitySaver implements IEntitySaver {

    Map<Class, IRelationalColumnSaver> relationalColumnSavers = new HashMap<>();

    public MariaDBEntitySaver() {
        initRelationalColumnSavers();
    }

    private void initRelationalColumnSavers() {
        relationalColumnSavers.put(OneToOne.class, new OneToOneColumnSaver());
        relationalColumnSavers.put(OneToMany.class, new OneToManyColumnSaver());
        relationalColumnSavers.put(ManyToMany.class, new ManyToManyColumnSaver());
        relationalColumnSavers.put(ManyToOne.class, new ManyToOneColumnSaver());
    }

    @Override
    public Object save(_ITable table, Object entity, ORM orm, Connection connection) {
        Object inheritanceID = null;
        //inheritSave
        if (table.getInheritanceColumn() != null) {
            inheritanceID = save(orm.getTables().get(table.getInheritanceColumn().getType()), entity, orm, connection); //Saves enity, garanties that inheritance object is saved an has id
        }


        //preRelationSave
        Map<_IRelationalColumn, Object> preRelationIdMap = new HashMap();

        for (_IRelationalColumn c : table.getRelationalColumns()) {
            Object relationalID = relationalColumnSavers.get(c.getAnnotationClass()).preInsertSave(table, entity, orm, c, this, connection);
            if (relationalID != null) {
                preRelationIdMap.put(c, relationalID);
            }
        }

        Object mainObjectID = null;
        Field mainObjectIDField = null;
        try {
            mainObjectIDField = table.getSClass().getDeclaredField(table.getId().getName());
            mainObjectIDField.setAccessible(true);
            mainObjectID = mainObjectIDField.get(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mainSave if id=null
        if (mainObjectID == null) {
            String saveString = "";
            try {
                saveString += "INSERT INTO " + table.getTableName() + " (";

                if (table.getInheritanceColumn() != null) {
                    saveString += table.getInheritanceColumn().getColumnName() + ",";
                }

                String columnsString = "";
                for (_IColumn c : table.getColumns()) {
                    if (columnsString == "")
                        columnsString += c.getColumnName();
                    else
                        columnsString += " ," + c.getColumnName();
                }
                saveString += columnsString;

                String relationalColumnsString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                        relationalColumnsString += " ," + c.getKey().getColumnName();
                }
                saveString += relationalColumnsString;

                saveString += ")\n VALUES(";

                if (table.getInheritanceColumn() != null) {
                    saveString += orm.getConverter().JavaToDBValue(inheritanceID) + ",";
                }

                String columnsValString = "";
                for (_IColumn c : table.getColumns()) {
                    if (columnsValString == "") {
                        Field field = table.getSClass().getDeclaredField(c.getName());
                        field.setAccessible(true);
                        columnsValString += orm.getConverter().JavaToDBValue(field.get(entity));
                    } else {
                        Field field = table.getSClass().getDeclaredField(c.getName());
                        field.setAccessible(true);
                        columnsValString += " ," + orm.getConverter().JavaToDBValue(field.get(entity));
                    }
                }
                saveString += columnsValString;

                String relationalColumnsValString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                    relationalColumnsValString += " ," + orm.getConverter().JavaToDBValue(c.getValue());
                }
                saveString += relationalColumnsValString + ") RETURNING " + table.getId().getColumnName() + ";";

            } catch (Exception e) {
                return saveString = "";
            }

            try (Statement stmt = connection.createStatement()) {
                if (saveString != "") {
                    ResultSet resultSet = stmt.executeQuery(saveString);
                    resultSet.first();
                    mainObjectIDField.set(entity, resultSet.getObject(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else//mainSave if id!=null
        {
            String updateString = "";
            try {
                updateString += "UPDATE " + table.getTableName() + " SET ";

                if (table.getInheritanceColumn() != null) {
                    updateString += table.getInheritanceColumn().getColumnName() + " = " + orm.getConverter().JavaToDBValue(inheritanceID) + ",\n";
                }

                String columnsString = "";
                for (_IColumn c : table.getColumns()) {
                    Field field = table.getSClass().getDeclaredField(c.getName());
                    field.setAccessible(true);
                    if (columnsString == "")
                        columnsString += c.getColumnName() + " = " + orm.getConverter().JavaToDBValue(field.get(entity));
                    else
                        columnsString += ",\n" + c.getColumnName() + " = " + orm.getConverter().JavaToDBValue(field.get(entity));
                }
                updateString += columnsString;

                String relationalColumnsString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                    relationalColumnsString += ",\n " + c.getKey().getColumnName() + " = " + orm.getConverter().JavaToDBValue(c.getValue());
                }
                updateString += relationalColumnsString;

                updateString += "\nWHERE "+ table.getId().getColumnName() + " = " + orm.getConverter().JavaToDBValue(mainObjectID)+";";

            } catch (Exception e) {
                return updateString = "";
            }

            try (Statement stmt = connection.createStatement()) {
                if (updateString != "") {
                    stmt.execute(updateString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //postRelationSave
        for (_IRelationalColumn c : table.getRelationalColumns()) {
            relationalColumnSavers.get(c.getAnnotationClass()).postInsertSave(table, entity, orm, c, this, connection);
        }

        try {
            return mainObjectIDField.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object saveWithAdditionalRelation(_ITable table, Object entity, ORM orm, Connection connection, String additionalRelationalColumn, Object additionalRelationalColumnID) {
        Object inheritanceID = null;
        //inheritSave
        if (table.getInheritanceColumn() != null) {
            inheritanceID = save(orm.getTables().get(table.getInheritanceColumn().getType()), entity, orm, connection); //Saves enity, garanties that inheritance object is saved an has id
        }


        //preRelationSave
        Map<_IRelationalColumn, Object> preRelationIdMap = new HashMap();

        for (_IRelationalColumn c : table.getRelationalColumns()) {
            Object relationalID = relationalColumnSavers.get(c.getAnnotationClass()).preInsertSave(table, entity, orm, c, this, connection);
            if (relationalID != null) {
                preRelationIdMap.put(c, relationalID);
            }
        }

        Object mainObjectID = null;
        Field mainObjectIDField = null;
        try {
            mainObjectIDField = table.getSClass().getDeclaredField(table.getId().getColumnName());
            mainObjectIDField.setAccessible(true);
            mainObjectID = mainObjectIDField.get(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mainSave if id=null
        if (mainObjectID == null) {
            String saveString = "";
            try {
                saveString += "INSERT INTO " + table.getTableName() + " (";

                if (table.getInheritanceColumn() != null) {
                    saveString += table.getInheritanceColumn().getColumnName() + ",";
                }

                String columnsString = "";
                for (_IColumn c : table.getColumns()) {
                    if (columnsString == "")
                        columnsString += c.getColumnName();
                    else
                        columnsString += " ," + c.getColumnName();
                }
                saveString += columnsString;

                String relationalColumnsString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                        relationalColumnsString += " ," +c.getKey().getColumnName();//relationalColumnSavers.get(c.getKey().getAnnotationClass()).whileDefinitionInsertSave(table, entity, orm, c.getKey(), this)
                }
                saveString += relationalColumnsString;

                saveString += ", " + additionalRelationalColumn;

                saveString += ")\n VALUES(";

                if (table.getInheritanceColumn() != null) {
                    saveString += orm.getConverter().JavaToDBValue(inheritanceID) + ",";
                }

                String columnsValString = "";
                for (_IColumn c : table.getColumns()) {
                    if (columnsValString == "") {
                        Field field = table.getSClass().getDeclaredField(c.getName());
                        field.setAccessible(true);
                        columnsValString += orm.getConverter().JavaToDBValue(field.get(entity));
                    } else {
                        Field field = table.getSClass().getDeclaredField(c.getName());
                        field.setAccessible(true);
                        columnsValString += " ," + orm.getConverter().JavaToDBValue(field.get(entity));
                    }
                }
                saveString += columnsValString;

                String relationalColumnsValString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                    relationalColumnsValString += " ," + orm.getConverter().JavaToDBValue(c.getValue());
                }
                saveString += relationalColumnsValString;
                saveString += ", " + orm.getConverter().JavaToDBValue(additionalRelationalColumnID);
                saveString += ") RETURNING " + table.getId().getColumnName() + ";";

            } catch (Exception e) {
                return saveString = "";
            }

            try (Statement stmt = connection.createStatement()) {
                if (saveString != "") {
                    ResultSet resultSet = stmt.executeQuery(saveString);
                    resultSet.first();
                    mainObjectIDField.set(entity, resultSet.getObject(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else//mainSave if id!=null
        {
            String updateString = "";
            try {
                updateString += "UPDATE " + table.getTableName() + " SET ";

                if (table.getInheritanceColumn() != null) {
                    updateString += table.getInheritanceColumn().getColumnName() + " = " + orm.getConverter().JavaToDBValue(inheritanceID) + ",\n";
                }

                String columnsString = "";
                for (_IColumn c : table.getColumns()) {
                    Field field = table.getSClass().getDeclaredField(c.getName());
                    field.setAccessible(true);
                    if (columnsString == "")
                        columnsString += c.getColumnName() + " = " + orm.getConverter().JavaToDBValue(field.get(entity));
                    else
                        columnsString += "\n," + c.getColumnName() + " = " + orm.getConverter().JavaToDBValue(field.get(entity));
                }
                updateString += columnsString;

                String relationalColumnsString = "";
                for (Map.Entry<_IRelationalColumn, Object> c : preRelationIdMap.entrySet()) {
                    relationalColumnsString += ",\n " + c.getKey().getColumnName() + " = " + orm.getConverter().JavaToDBValue(c.getValue());
                }
                updateString += relationalColumnsString;

                updateString += ",\n" + additionalRelationalColumn + " = " + orm.getConverter().JavaToDBValue(additionalRelationalColumnID);

                updateString += "\nWHERE "+ table.getId().getColumnName() + " = " + orm.getConverter().JavaToDBValue(mainObjectID)+";";

            } catch (Exception e) {
                return updateString = "";
            }

            try (Statement stmt = connection.createStatement()) {
                if (updateString != "") {
                    stmt.execute(updateString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //postRelationSave
        for (_IRelationalColumn c : table.getRelationalColumns()) {
            relationalColumnSavers.get(c.getAnnotationClass()).postInsertSave(table, entity, orm, c, this, connection);
        }

        try {
            return mainObjectIDField.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
