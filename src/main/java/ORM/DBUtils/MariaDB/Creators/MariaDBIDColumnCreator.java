package ORM.DBUtils.MariaDB.Creators;

import ORM.Annotations.Data.InTable._IIDColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Creators.IIDColumnCreator;

public class MariaDBIDColumnCreator implements IIDColumnCreator {
    @Override
    public String createColumnQuery(_IIDColumn c, _ITable currT) {
        return "    " + c.getColumnName() + " " + c.getColumnType() + (c.getNullable() ? "" : " NOT NULL")+ (c.getAutoIncrement() ? " AUTO_INCREMENT" : "")+",\n    CONSTRAINT "+currT.getTableName()+"PK PRIMARY KEY ("+c.getColumnName()+"),\n";
    }
}
