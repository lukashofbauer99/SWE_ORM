package ORM.DBUtils.General.Creators;

import ORM.Annotations.Data.InTable._IColumn;
import ORM.Annotations.Data.InTable._IIDColumn;
import ORM.Annotations.Data._ITable;

public interface IIDColumnCreator {

    /** Creates String to create ID Column
     *
     * @param c id Column that String is created for
     * @param currT table where the columns belongs to
     * @return id column creation query string
     */
    String createColumnQuery(_IIDColumn c, _ITable currT);
}
