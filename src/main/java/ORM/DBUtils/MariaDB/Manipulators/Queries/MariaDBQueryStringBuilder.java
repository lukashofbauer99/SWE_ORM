package ORM.DBUtils.MariaDB.Manipulators.Queries;

import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Manipulators.Queries.IQueryStringBuilder;
import ORM.ORM;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class MariaDBQueryStringBuilder<T> implements IQueryStringBuilder<T> {

    IQueryStringBuilder<T> prev;

    String queryString="";

    ORM orm;

    Class<T> c;

    @Override
    public IQueryStringBuilder fillQueryStringBuilder(Class<T> c,ORM orm, String s,IQueryStringBuilder prev) {
        this.orm=orm;
        this.queryString+=s;
        this.c=c;
        this.prev=prev;
        return this;
    }

    @Override
    public IQueryStringBuilder fillQueryStringBuilder(Class<T> c,ORM orm) {
        this.orm=orm;
        this.c=c;
        return this;
    }

    @Override
    public IQueryStringBuilder where(String fieldName) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,
                "WHERE m."+orm.getTables().get(c).getColumns().stream().filter(x->x.getName()==fieldName).findFirst().get().getColumnName(),this);
    }

    @Override
    public IQueryStringBuilder and(String fieldName) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,
                " AND m."+orm.getTables().get(c).getColumns().stream().filter(x->x.getName()==fieldName).findFirst().get().getColumnName(),this);
    }

    @Override
    public IQueryStringBuilder or(String fieldName) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,
                " OR m."+orm.getTables().get(c).getColumns().stream().filter(x->x.getName()==fieldName).findFirst().get().getColumnName(),this);
    }

    @Override
    public IQueryStringBuilder constVal(Object o) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,orm.getConverter().JavaToDBValue(o),this);
    }

    @Override
    public IQueryStringBuilder columnVal(String fieldName) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,
                "m."+orm.getTables().get(c).getColumns().stream().filter(x->x.getName()==fieldName).findFirst().get().getColumnName(),this);
    }

    @Override
    public IQueryStringBuilder comparer(String comparer) {
        return new MariaDBQueryStringBuilder().fillQueryStringBuilder(this.c,this.orm,comparer,this);
    }

    @Override
    public String build() {
        if(prev!=null)
            return prev.build()+queryString;
        else
            return queryString;
    }

    @Override
    public Collection<T> execute() {
        if (!orm.getTables().isEmpty()) {
            Collection<T> returnObjects = new ArrayList<>();

            _ITable table = orm.getTables().get(c);
            String getString = "SELECT ";

            getString += "m." + table.getId().getColumnName();

            getString += " FROM " + table.getTableName() + " m ";


            getString += build() + ";";

            try (Statement stmt = orm.getConnection().createStatement()) {
                if (getString != "") {
                    ResultSet resultSet = stmt.executeQuery(getString);
                    int resultSetColumnCounter = 1;
                    while (resultSet.next()) {
                        returnObjects.add(orm.get(c, orm.getConverter().DBToJavaValue(resultSet.getObject(resultSetColumnCounter), table.getId().getType())));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnObjects;
        }
        else
            return null;
    }
}
