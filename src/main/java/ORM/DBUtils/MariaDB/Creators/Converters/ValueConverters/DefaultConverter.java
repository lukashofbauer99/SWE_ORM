package ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters;

import ORM.DBUtils.General.Creators.Converters.IValueConverter;

public class DefaultConverter implements IValueConverter {
    @Override
    public String convertToDB(Object value) {
        return value.toString();
    }

    @Override
    public Object convertToJava(Class c,Object value) {
        return value;
    }
}
