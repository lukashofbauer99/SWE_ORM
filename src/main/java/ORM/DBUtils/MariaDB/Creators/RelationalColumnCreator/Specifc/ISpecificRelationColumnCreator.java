package ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

public interface ISpecificRelationColumnCreator {

    String createSpecificColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm);
}
