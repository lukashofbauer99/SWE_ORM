package ORM.DBUtils.MariaDB.Creators;

import ORM.Annotations.Data.InTable._IColumn;
import ORM.DBUtils.General.Creators.IColumnCreator;

public class MariaDBColumnCreator implements IColumnCreator {
    @Override
    public String createColumnQuery(_IColumn c) {
        return "    " + c.getColumnName() + " " + c.getColumnType() + (c.getNullable() ? "" : " NOT NULL");
    }
}
