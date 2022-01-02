package ORM.DBUtils.General.Manipulators.Deleters;

import ORM.ORM;

import java.sql.Connection;

public interface IEntityDeleter {

    /** deletes enity o from database
     *
     * @param o entity to delete
     * @param orm orm
     * @param connection connection used
     */
    void delete(Object o, ORM orm, Connection connection);

}
