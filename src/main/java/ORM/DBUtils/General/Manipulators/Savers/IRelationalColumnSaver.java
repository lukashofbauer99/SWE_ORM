package ORM.DBUtils.General.Manipulators.Savers;

import ORM.Annotations.Data.InTable.Relations._IRelationalColumn;
import ORM.Annotations.Data._ITable;
import ORM.ORM;

import java.sql.Connection;

public interface IRelationalColumnSaver {

    /** used for relations that need to be saved before main entity so that thay can be referenced(OneToOne, ...)
     *
     * @param t table of main entity
     * @param entity main entity
     * @param orm orm
     * @param relationalColumn relational column that needs to be persisted
     * @param tableSaver table saver that called this method
     * @param connection connection that is used
     * @return id of relation entity
     */
    Object preInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection);

    /** used for relations that need the main entity to be saved so that thay can be saved(ManyToMany, ...)
     *
     * @param t table of main entity
     * @param entity main entity
     * @param orm orm
     * @param relationalColumn relational column that needs to be persisted
     * @param tableSaver table saver that called this method
     * @param connection connection that is used
     * @return if save was successful
     */
    boolean postInsertSave(_ITable t, Object entity, ORM orm, _IRelationalColumn relationalColumn, IEntitySaver tableSaver, Connection connection);

}
