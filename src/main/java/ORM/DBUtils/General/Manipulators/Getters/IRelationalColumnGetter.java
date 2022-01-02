package ORM.DBUtils.General.Manipulators.Getters;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.ORM;

import java.sql.Connection;

public interface IRelationalColumnGetter {

    /** gets relational entity/ies from entity with id
     *
     * @param c class of entity
     * @param id id of entity
     * @param orm orm
     * @param connection connection used
     * @param col relational column that is loaded
     * @return relational entity/ies from entity with id
     */
    Object get(Class c, Object id, ORM orm, Connection connection, _IRelationalColumn col);
}
