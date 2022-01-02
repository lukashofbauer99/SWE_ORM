package ORM.DBUtils.General.Creators;

import ORM.Annotations.Data._ITable;
import ORM.ORM;

public interface ITableCreator {

    /** Creates creation query string for normal columns
     *
     * @param t table for that creation query string is created
     * @param c column creator used
     * @param cid id column creator used
     * @return creation query string for normal columns
     */
    String createTableColumnsQuery(_ITable t, IColumnCreator c,IIDColumnCreator cid);

    /** Creates creation query string for inheritance column
     *
     * @param t table for that creation query string is created
     * @param rc column creator used
     * @param orm orm
     * @return creation query string for inheritance column
     */
    String createTableInheritanceColumnQuery(_ITable t, IInheritanceColumnCreator rc, ORM orm);

    /** Creates creation query string for relational column
     *
     * @param t table for that creation query string is created
     * @param rc column creator used
     * @param orm orm
     * @return creation query string for relational column
     */
    String createTableRelationalColumnsQuery(_ITable t, IRelationalColumnCreator rc, ORM orm);
}
