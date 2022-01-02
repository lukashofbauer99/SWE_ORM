package ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters.SpecialValueConverters;

import ORM.DBUtils.General.Creators.Converters.ISpecialValueConverter;

public class EnumValueConverter implements ISpecialValueConverter {
    @Override
    public boolean isType(Class t) {
        boolean bo = t.isEnum();
        return bo;
    }

    @Override
    public String convertToDB(Object value) {
        return "'"+value+"'";
    }

    @Override
    public Object convertToJava(Class c, Object value) {
        if (value!=null)
            return Enum.valueOf(c,(String) value);
        return null;
    }

}
