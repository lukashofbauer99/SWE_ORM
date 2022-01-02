package ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator;

import ORM.Annotations.Data.InTable.Relations.*;
import ORM.Annotations.Data._ITable;
import ORM.DBUtils.General.Creators.IRelationalColumnCreator;
import ORM.DBUtils.MariaDB.Creators.RelationalColumnCreator.Specifc.*;
import ORM.ORM;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MariaDBRelationalColumnCreator implements IRelationalColumnCreator {

    Map<Type, ISpecificRelationColumnCreator> specificRelationColumnCreatorMap = new HashMap<>();

    public MariaDBRelationalColumnCreator() {
        specificRelationColumnCreatorMap.put(_OneToOne.class, new OneToOneColumnCreator());
        specificRelationColumnCreatorMap.put(_ManyToMany.class, new ManyToManyColumnCreator());
        specificRelationColumnCreatorMap.put(_ManyToOne.class, new ManyToOneColumnCreator());
        specificRelationColumnCreatorMap.put(_OneToMany.class, new OneToManyColumnCreator());

    }

    @Override
    public String createColumnQuery(_IRelationalColumn c, _ITable currT, ORM orm) {
        return specificRelationColumnCreatorMap.get(c.getClass()).createSpecificColumnQuery(c, currT, orm);
    }
}
