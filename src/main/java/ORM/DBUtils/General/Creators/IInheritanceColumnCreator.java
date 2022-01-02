package ORM.DBUtils.General.Creators;

import ORM.Annotations.Data.InTable.Inheritance._IInheritaceColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

public interface IInheritanceColumnCreator {

    /** Creates String to create inheritance relation column
     *
     * @param c inheritance column that String is created for
     * @param currT table that column belongs to
     * @param orm orm
     * @return inheritance column creation query string
     */
    String createColumnQuery(_IInheritaceColumn c, _ITable currT, ORM orm);
}
