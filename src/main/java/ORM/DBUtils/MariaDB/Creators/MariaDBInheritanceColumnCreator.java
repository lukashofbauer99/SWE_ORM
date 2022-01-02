package ORM.DBUtils.MariaDB.Creators;

import ORM.Annotations.Data.InTable.Inheritance._IInheritaceColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Creators.IInheritanceColumnCreator;
import ORM.ORM;

public class MariaDBInheritanceColumnCreator implements IInheritanceColumnCreator {
    @Override
    public String createColumnQuery(_IInheritaceColumn c, _ITable currT, ORM orm) {
        return "ALTER TABLE " + currT.getTableName() + "\nADD COLUMN " + c.getColumnName() + " " + c.getColumnType() + " " + (c.getNullable() ? "" : "NOT NULL") +
                ",\nADD CONSTRAINT " + c.getName()+"_"+currT.getTableName() + "FK FOREIGN KEY (" + c.getColumnName() +
                ")\nREFERENCES " + orm.getTables().get(c.getType()).getTableName() + " (" + orm.getTables().get(c.getType()).getId().getColumnName() + ") " +
                "\nON DELETE CASCADE" +
                "\nON UPDATE CASCADE;";
    }
}
