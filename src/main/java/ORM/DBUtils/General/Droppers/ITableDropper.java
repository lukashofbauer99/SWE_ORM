package ORM.DBUtils.General.Droppers;

import ORM.Annotations.Data._ITable;

public interface ITableDropper {

    String drop(_ITable t);
}
