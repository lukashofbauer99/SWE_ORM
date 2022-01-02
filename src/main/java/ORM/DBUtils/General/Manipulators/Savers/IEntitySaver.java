package ORM.DBUtils.General.Manipulators.Savers;

import ORM.Annotations.Data._ITable;
import ORM.ORM;

import java.sql.Connection;

public interface IEntitySaver {

    /** saves entity
     *
     * @param table table of entity that is saved
     * @param entity entity that is saved
     * @param orm orm
     * @param connection connection that is used
     * @return saved entity
     */
    Object save(_ITable table, Object entity, ORM orm, Connection connection);

    /** saves entity with additional relational column
     *
     * @param table table of entity that is saved
     * @param entity entity that is saved
     * @param orm orm
     * @param connection connection that is used
     * @param additionalRelationalColumn name of additional relational column that is saved
     * @param additionalRelationalColumnID id of additional relational column that is saved
     * @return saved entity
     */
    Object saveWithAdditionalRelation(_ITable table, Object entity, ORM orm, Connection connection,String additionalRelationalColumn,Object additionalRelationalColumnID);
}
