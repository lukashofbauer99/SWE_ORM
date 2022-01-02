package ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

public class OneToManyColumnCreator implements ISpecificRelationColumnCreator {
    @Override
    public String createSpecificColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm) {
        return "ALTER TABLE " + orm.getTables().get(c.getType()).getTableName() +
                "\n ADD COLUMN " + c.getName()+"_"+currT.getTableName() + " " + currT.getId().getColumnType() + " " + (c.getNullable() ? "" : "NOT NULL") +
                ",\n ADD CONSTRAINT " + c.getName()+"_"+currT.getTableName()+ "FK FOREIGN KEY (" +  c.getName()+"_"+currT.getTableName() +
                ")\n REFERENCES " + currT.getTableName() + " (" + currT.getId().getColumnName() + ")"+
                "\n ON DELETE "+c.getOnDelete() +
                "\n ON UPDATE "+c.getOnUpdate()+";";

    }
}
