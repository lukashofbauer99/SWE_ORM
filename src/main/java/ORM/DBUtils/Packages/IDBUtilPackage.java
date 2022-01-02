package ORM.DBUtils.Packages;

import ORM.DBUtils.General.Creators.*;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.DBUtils.General.Droppers.ITableDropper;
import ORM.DBUtils.General.Manipulators.Deleters.IEntityDeleter;
import ORM.DBUtils.General.Manipulators.Getters.IEntityGetter;
import ORM.DBUtils.General.Manipulators.Queries.IQueryStringBuilder;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;

public interface IDBUtilPackage {
    IConverter getConverter();
    IColumnCreator getColumnCreator();
    IIDColumnCreator getIidColumnCreator();
    IRelationalColumnCreator getRelationalColumnCreator();
    IInheritanceColumnCreator getInheritanceColumnCreator();
    ITableCreator getTableCreator();
    ITableDropper getTableDropper();

    IEntitySaver getEntitySaver();
    IEntityDeleter getEntityDeleter();
    IEntityGetter getEntityGetter();
    IQueryStringBuilder getQueryStringBuilder();
}
