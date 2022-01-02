package ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

public class OneToOneColumnCreator implements ISpecificRelationColumnCreator {
    @Override
    public String createSpecificColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm) {
        return "ALTER TABLE " + currT.getTableName() +
                "\n ADD COLUMN " +c.getColumnName() + " " + c.getColumnType() + " " + (c.getNullable() ? "" : "NOT NULL") +
                ",\n ADD CONSTRAINT " +c.getName()+"_"+currT.getTableName() + "FK FOREIGN KEY (" + c.getColumnName() +
                ")\n REFERENCES " + orm.getTables().get(c.getType()).getTableName() + " (" + orm.getTables().get(c.getType()).getId().getColumnName() + ")"+
                "\n ON DELETE "+c.getOnDelete() +
                "\n ON UPDATE "+c.getOnUpdate()+";";
    }
}
