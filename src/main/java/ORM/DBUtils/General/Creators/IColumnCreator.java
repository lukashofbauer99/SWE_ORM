package ORM.DBUtils.General.Creators;

import ORM.Annotations.Data.InTable._IColumn;

public interface IColumnCreator {

    /** Creates String to create Column
     *
     * @param c Column that String is created for
     * @return column creation query string
     */
    String createColumnQuery(_IColumn c);
}
