package ORM.DBUtils.MariaDB.Droppers;

import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Droppers.ITableDropper;

public class MariaDBTableDropper implements ITableDropper {
    @Override
    public String drop(_ITable t) {
        return "DROP TABLE IF EXISTS "+t.getTableName()+";";
    }
}
