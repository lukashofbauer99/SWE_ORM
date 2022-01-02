package ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc.ISpecificRelationColumnCreator;
import ORM.ORM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManyToManyColumnCreator implements ISpecificRelationColumnCreator {

    Map<String, _IRelationalColumn> registeredManyToManys = new HashMap<>();

    @Override
    public String createSpecificColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm) {
        String returnString = "";

        String identifier= orm.getTables().get(c.getType()).getTableName() + "_" + currT.getSClass();
        if (registeredManyToManys.get(identifier) != null) {

            List<String> nameOrder= new ArrayList<>();
            nameOrder.add(currT.getTableName());
            nameOrder.add(orm.getTables().get(c.getType()).getTableName());
            java.util.Collections.sort(nameOrder);

            returnString = "CREATE OR REPLACE TABLE " + nameOrder.get(0)+ "_" + nameOrder.get(1) + "( \n" +
                    currT.getTableName() + "ID " + currT.getId().getColumnType() + " NOT NULL, \n" +
                    orm.getTables().get(c.getType()).getTableName() + "ID " + orm.getTables().get(c.getType()).getId().getColumnType() + " NOT NULL, \n" +
                    "PRIMARY KEY (" + currT.getTableName() + "ID, " + orm.getTables().get(c.getType()).getTableName() + "ID), \n" +
                    "CONSTRAINT " + c.getName()+"_"+currT.getTableName() + "FK FOREIGN KEY (" + currT.getTableName() + "ID) REFERENCES " + currT.getTableName() + "(" + currT.getId().getColumnName() + ")" +
                    "\nON DELETE "+c.getOnDelete()+"\n ON UPDATE "+c.getOnUpdate() +", \n" +
                    "CONSTRAINT " + registeredManyToManys.get(identifier).getName()+"_"+orm.getTables().get(c.getType()).getTableName() + "FK FOREIGN KEY (" + orm.getTables().get(c.getType()).getTableName() + "ID) REFERENCES " + orm.getTables().get(c.getType()).getTableName() + "(" + orm.getTables().get(c.getType()).getId().getColumnName() + ")"+
                    "\nON DELETE "+registeredManyToManys.get(identifier).getOnDelete()+"\n ON UPDATE "+registeredManyToManys.get(identifier).getOnUpdate() +"\n);";
        } else {

            registeredManyToManys.put(currT.getTableName() + "_" + c.getType(), c);

        }

        return returnString;
    }
}
