package ORM.DBUtils.MariaDB.Creators.Converters.SpecialTypes;

import ORM.DBUtils.General.Creators.Converters.ISpecialTypeConverter;

public class EnumTypeConverter implements ISpecialTypeConverter {
    @Override
    public boolean isType(Class t) {
        boolean bo = t.isEnum();
        return bo;
    }

    @Override
    public String convert(Class t) {
        return "VARCHAR(255)";
    }
}
