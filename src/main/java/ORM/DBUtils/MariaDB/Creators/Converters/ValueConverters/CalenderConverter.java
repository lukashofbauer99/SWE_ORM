package ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters;

import ORM.DBUtils.General.Creators.Converters.IValueConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalenderConverter implements IValueConverter {

    @Override
    public String convertToDB(Object value) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "'"+f.format(((Calendar) value).getTime())+"'";
    }

    @Override
    public Object convertToJava(Class c,Object value) {
        if(value!=null) {
            Calendar returnValue = Calendar.getInstance();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                returnValue.setTime(f.parse((String) value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return returnValue;
        }
        return null;
    }
}
