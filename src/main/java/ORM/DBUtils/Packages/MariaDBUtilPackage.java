package ORM.DBUtils.Packages;

import ORM.DBUtils.General.Creators.*;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.DBUtils.General.Droppers.ITableDropper;
import ORM.DBUtils.General.Manipulators.Deleters.IEntityDeleter;
import ORM.DBUtils.General.Manipulators.Getters.IEntityGetter;
import ORM.DBUtils.General.Manipulators.Queries.IQueryStringBuilder;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.DBUtils.MariaDB.Creators.Converters.MariaDBTypeConverter;
import ORM.DBUtils.MariaDB.Creators.MariaDBColumnCreator;
import ORM.DBUtils.MariaDB.Creators.MariaDBIDColumnCreator;
import ORM.DBUtils.MariaDB.Creators.MariaDBInheritanceColumnCreator;
import ORM.DBUtils.MariaDB.Creators.MariaDBTableCreator;
import ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.MariaDBRelationalColumnCreator;
import ORM.DBUtils.MariaDB.Droppers.MariaDBTableDropper;
import ORM.DBUtils.MariaDB.Manipulators.Deleters.MariaDBEntityDeleter;
import ORM.DBUtils.MariaDB.Manipulators.Getters.MariaDBEntityGetter;
import ORM.DBUtils.MariaDB.Manipulators.Queries.MariaDBQueryStringBuilder;
import ORM.DBUtils.MariaDB.Manipulators.Savers.MariaDBEntitySaver;
import lombok.Getter;

@Getter
public class MariaDBUtilPackage implements IDBUtilPackage{
    IConverter converter = new MariaDBTypeConverter();
    IColumnCreator columnCreator = new MariaDBColumnCreator();
    IIDColumnCreator iidColumnCreator = new MariaDBIDColumnCreator();
    IRelationalColumnCreator relationalColumnCreator = new MariaDBRelationalColumnCreator();
    IInheritanceColumnCreator inheritanceColumnCreator = new MariaDBInheritanceColumnCreator();
    ITableCreator tableCreator = new MariaDBTableCreator();
    ITableDropper tableDropper = new MariaDBTableDropper();

    IEntitySaver entitySaver = new MariaDBEntitySaver();
    IEntityDeleter entityDeleter = new MariaDBEntityDeleter();
    IEntityGetter entityGetter = new MariaDBEntityGetter();
    IQueryStringBuilder queryStringBuilder=new MariaDBQueryStringBuilder();
}
