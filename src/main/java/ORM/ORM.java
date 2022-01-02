package ORM;

import ORM.Annotations.Data._BasicTable;
import ORM.Annotations.Data._ITable;
import ORM.Annotations.Table;
import ORM.Cache.ICache;
import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.DBUtils.General.Creators.*;
import ORM.DBUtils.General.Droppers.ITableDropper;
import ORM.DBUtils.General.Manipulators.Deleters.IEntityDeleter;
import ORM.DBUtils.General.Manipulators.Getters.IEntityGetter;
import ORM.DBUtils.General.Manipulators.Queries.IQueryStringBuilder;
import ORM.DBUtils.General.Manipulators.Savers.IEntitySaver;
import ORM.DBUtils.Packages.IDBUtilPackage;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class ORM {

    HashMap<Class, _ITable> tables = new HashMap<>();
    IConverter converter = null;
    IColumnCreator columnCreator = null;
    IIDColumnCreator iidColumnCreator = null;
    IRelationalColumnCreator relationalColumnCreator = null;
    IInheritanceColumnCreator inheritanceColumnCreator = null;
    ITableCreator tableCreator = null;
    ITableDropper tableDropper=null;

    IEntitySaver entitySaver = null;
    IEntityDeleter entityDeleter = null;
    IEntityGetter entityGetter = null;
    IQueryStringBuilder queryStringBuilder=null;
    Connection connection;

    ICache cache = null;


    public ORM(String path,ClassLoader classLoader, String connectionString, String username, String password
            , IConverter typeConverter, IColumnCreator columnCreator, IIDColumnCreator iidColumnCreator, IRelationalColumnCreator relationalColumnCreator, IInheritanceColumnCreator inheritanceColumnCreator, ITableCreator tableCreator
            ,ITableDropper tableDropper, IEntitySaver tableSaver, IEntityDeleter entityDeleter, IEntityGetter entityGetter, IQueryStringBuilder queryStringBuilder, ICache cache) throws SQLException {
        connection = DriverManager.getConnection(connectionString+"?allowMultiQueries=true", username, password);
        this.converter = typeConverter;
        this.columnCreator = columnCreator;
        this.iidColumnCreator = iidColumnCreator;
        this.relationalColumnCreator = relationalColumnCreator;
        this.inheritanceColumnCreator = inheritanceColumnCreator;
        this.tableCreator = tableCreator;
        this.tableDropper=tableDropper;
        this.entitySaver = tableSaver;
        this.entityDeleter=entityDeleter;
        this.entityGetter= entityGetter;
        this.queryStringBuilder=queryStringBuilder;
        this.cache = cache;
        if(cache!=null) {
            cache.init(this);
        }
        scanPackageAndInit(path,classLoader);
    }
    public ORM(String path,ClassLoader classLoader, String connectionString, String username, String password,
            IDBUtilPackage utils, ICache cache) throws SQLException {
        connection = DriverManager.getConnection(connectionString+"?allowMultiQueries=true", username, password);
        this.converter = utils.getConverter();
        this.columnCreator = utils.getColumnCreator();
        this.iidColumnCreator = utils.getIidColumnCreator();
        this.relationalColumnCreator = utils.getRelationalColumnCreator();
        this.inheritanceColumnCreator = utils.getInheritanceColumnCreator();
        this.tableCreator = utils.getTableCreator();
        this.tableDropper=utils.getTableDropper();
        this.entitySaver = utils.getEntitySaver();
        this.entityDeleter= utils.getEntityDeleter();
        this.entityGetter= utils.getEntityGetter();
        this.queryStringBuilder=utils.getQueryStringBuilder();
        this.cache = cache;
        if(cache!=null) {
            cache.init(this);
        }

        scanPackageAndInit(path,classLoader);
    }

    /** scans path and inits meta data
     *
     * @param path path that is scanned
     * @return true if it was successful
     */
    public boolean scanPackageAndInit(String path,ClassLoader classLoader) {
        AtomicBoolean success = new AtomicBoolean(true);
        Set<Class<?>> list = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(path)
                        .filterInputsBy(new FilterBuilder().includePackage(path))).getTypesAnnotatedWith(Table.class);


        list.forEach(c ->
        {
            _ITable table = new _BasicTable();
            try {
                table.basicInit(c, converter, this);
                tables.put(c, table);
            } catch (Exception e) {
                success.set(false);
            }
        });

        if (success.get()) {
            list.forEach(c ->
            {
                tables.get(c).inheritanceInit(c, converter, this);
                tables.get(c).relationalInit(c, converter, this);
            });
        }
        return success.get();
    }

    /** creates database
     *
     * @return true if it was successful
     */
    public boolean createDatabase() {
        AtomicBoolean success = new AtomicBoolean(true);
        tables.forEach((x, y) -> {
            System.out.println(tableCreator.createTableColumnsQuery(y, columnCreator, iidColumnCreator));
            try {
                try (Statement stmt = connection.createStatement()) {
                    String statement= tableCreator.createTableColumnsQuery(y, columnCreator, iidColumnCreator);
                    if((!statement.isBlank())&&statement!=null)
                        stmt.execute(statement);
                }
            } catch (SQLException e) {
                success.set(false);
                e.printStackTrace();
            }
        });

        tables.forEach((x, y) -> {
            System.out.print("Inh Query:"+tableCreator.createTableInheritanceColumnQuery(y, inheritanceColumnCreator, this));
            System.out.print("Rel Query:"+tableCreator.createTableRelationalColumnsQuery(y, relationalColumnCreator, this));


            try (Statement stmt = connection.createStatement()) {
                String statement=tableCreator.createTableInheritanceColumnQuery(y, inheritanceColumnCreator, this);

                if((!statement.isBlank())&&statement!=null)
                    stmt.execute(statement);
            } catch (Exception throwables) {
                success.set(false);
                throwables.printStackTrace();
            }


            try (Statement stmt = connection.createStatement()) {
                String statement=tableCreator.createTableRelationalColumnsQuery(y, relationalColumnCreator, this);

                if((!statement.isBlank())&&statement!=null)
                    stmt.execute(statement);
            } catch (Exception throwables) {
                success.set(false);
                throwables.printStackTrace();
            }
            System.out.println();

        });
        return success.get();
    }

    public boolean dropDatabase() {
        AtomicBoolean success = new AtomicBoolean(true);
        AtomicReference<String> dropStatment= new AtomicReference<>("SET FOREIGN_KEY_CHECKS = 0;\n");
        tables.forEach((x, y) -> {
            System.out.println(tableDropper.drop(y));
            dropStatment.set(dropStatment.get()+tableDropper.drop(y)+"\n");

        });
        dropStatment.set(dropStatment.get()+"SET FOREIGN_KEY_CHECKS = 1;");


        try {
            try (Statement stmt = connection.createStatement()) {
                if((!dropStatment.get().isBlank())&&dropStatment.get()!=null)
                    stmt.execute(dropStatment.get());
            }
        } catch (SQLException e) {
            success.set(false);
            e.printStackTrace();
        }
        return success.get();
    }

    /** saves entity
     *
     * @param entity entity to be saved
     * @return saved entity
     */
    public Object save(Object entity) {
        _ITable table = tables.get(entity.getClass());
        if (table != null) {
            Object o =entitySaver.save(table, entity, this,connection);
            if(cache != null) { cache.put(entity); }
            return o;
        } else
            return null;
    }

    /** gets entity with pk
     *
     * @param t class of entity
     * @param pk primary key of entity
     * @param <T> class of entity
     * @return entity
     */
    public <T> T get(Class<T> t, Object pk)
    {
        T entity= null;
        if(!tables.isEmpty()) {
            if (cache != null) {
                entity = (T) cache.get(t, pk);
            }
            if (entity == null) {
                entity = (T) entityGetter.get(t, pk, this, connection);
            }
        }
        return entity;
    }

    /** gets query string builder
     *
     * @param t class that is queried on
     * @param <T> class that is queried on
     * @return returns inited query string builder
     */
    public <T> IQueryStringBuilder<T> from(Class<T> t)
    {
        IQueryStringBuilder qsb = null;
            try {
                qsb = queryStringBuilder.getClass().getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return qsb.fillQueryStringBuilder(t, this);
    }

    /** deletes entity
     *
     * @param entity entity to delete
     */
    public void delete(Object entity)
    {
        if(!tables.isEmpty()) {
            entityDeleter.delete(entity, this, connection);
            if (cache != null) {
                cache.remove(entity);
            }
        }
    }

    /** gets id of enitity
     *
     * @param entity entity to get id from
     * @return id of entity
     */
    public Object getID(Object entity)
    {
        try {
            Field idField =  entity.getClass().getDeclaredField(tables.get(entity.getClass()).getId().getName());
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
