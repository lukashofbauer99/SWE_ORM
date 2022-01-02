package ORM.DBUtils.General.Manipulators.Getters;

import ORM.ORM;

import java.sql.Connection;

public interface IEntityGetter {

    /** Gets entity for id
     *
     * @param c class of entity
     * @param id id of entity
     * @param orm orm
     * @param connection connection used
     * @return entity
     */
    Object get(Class c, Object id, ORM orm, Connection connection);
}
