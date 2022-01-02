package ORM.DBUtils.MariaDB.Creators;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Creators.*;
import ORM.ORM;

public class MariaDBTableCreator implements ITableCreator {
    @Override
    public String createTableColumnsQuery(_ITable t, IColumnCreator c, IIDColumnCreator cid) {
        String tableString =
                "CREATE OR REPLACE TABLE " + t.getTableName() + "(\n";

        if (t.getId() != null)
            tableString += cid.createColumnQuery(t.getId(),t);
        String columnsString = "";
        for (_IColumn column : t.getColumns()) {
            if(columnsString!="")
            {
                columnsString += ",\n";
            }
            columnsString += c.createColumnQuery(column);
        }
        tableString += columnsString;
        tableString += "\n);";

        return tableString;
    }

    @Override
    public String createTableInheritanceColumnQuery(_ITable t, IInheritanceColumnCreator rc, ORM orm) {
        String tableString = "";

        if (t.getInheritanceColumn() != null) {
            tableString += "\n";
            tableString += rc.createColumnQuery(t.getInheritanceColumn(), t, orm);
        }
        return tableString;
    }

    @Override
    public String createTableRelationalColumnsQuery(_ITable t, IRelationalColumnCreator rc, ORM orm) {
        String tableString = "";

        for (_IRelationalColumn column : t.getRelationalColumns()) {
            if(tableString=="")
                tableString += rc.createColumnQuery(column, t, orm);
            else
                tableString += "\n"+rc.createColumnQuery(column, t, orm);
        }

        return tableString;
    }
}
