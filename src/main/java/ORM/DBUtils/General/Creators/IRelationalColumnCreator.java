package ORM.DBUtils.General.Creators;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

public interface IRelationalColumnCreator {

    /** Creates String to create relational relation column
     *
     * @param c relational column that String is created for
     * @param currT table that column belongs to
     * @param orm orm
     * @return relational column creation query string
     */
    String createColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm);
}
