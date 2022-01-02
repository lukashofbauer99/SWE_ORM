package ORM.DBUtils.MariaDB.Creators.Converters;

import ORM.DBUtils.General.Creators.Converters.IConverter;
import ORM.DBUtils.General.Creators.Converters.ISpecialTypeConverter;
import ORM.DBUtils.General.Creators.Converters.IValueConverter;
import ORM.DBUtils.MariaDB.Creators.Converters.SpecialTypes.EnumTypeConverter;
import ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters.CalenderConverter;
import ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters.DefaultConverter;
import ORM.DBUtils.General.Creators.Converters.ISpecialValueConverter;
import ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters.SpecialValueConverters.EnumValueConverter;
import ORM.DBUtils.MariaDB.Creators.Converters.ValueConverters.StringConverter;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MariaDBTypeConverter implements IConverter {

    private final Map<Type, String> typesConverters = new HashMap<>();
    private final List<ISpecialTypeConverter> specialTypeConverters = new ArrayList<>();

    private final Map<Type, IValueConverter> valueConverters = new HashMap<>();
    private final List<ISpecialValueConverter> specialValueConverters = new ArrayList<>();



    public MariaDBTypeConverter() {
        initTypeConverters();
        initSpecialTypeConverters();
        initValueConverters();
        initSpecialValueConverters();
    }

    private void initSpecialTypeConverters() {
        specialTypeConverters.add(new EnumTypeConverter());
    }
    private void initTypeConverters() {
        typesConverters.put(String.class, "VARCHAR(255)");

        typesConverters.put(Integer.class, "INT");
        typesConverters.put(int.class, "INT");

        typesConverters.put(boolean.class, "BOOLEAN");
        typesConverters.put(Boolean.class, "BOOLEAN");

        typesConverters.put(float.class, "FLOAT");
        typesConverters.put(Float.class, "FLOAT");

        typesConverters.put(long.class, "BIGINT");
        typesConverters.put(Long.class, "BIGINT");

        typesConverters.put(double.class, "DOUBLE");
        typesConverters.put(Double.class, "DOUBLE");

        typesConverters.put(Date.class, "VARCHAR(255)");
        typesConverters.put(Date.class, "VARCHAR(255)");

        typesConverters.put(Calendar.class, "VARCHAR(255)");
        typesConverters.put(GregorianCalendar.class, "VARCHAR(255)");

    }

    private void initSpecialValueConverters() {
        specialValueConverters.add(new EnumValueConverter());
    }
    private void initValueConverters()
    {
        valueConverters.put(String.class,new StringConverter());
        valueConverters.put(Calendar.class, new CalenderConverter());
        valueConverters.put(GregorianCalendar.class, new CalenderConverter());

    }


    @Override
    public String JavaToDBType(Class t) {

        AtomicReference<String> ret = new AtomicReference<>(typesConverters.get(t));
        if (ret.get() == null)
            specialTypeConverters.forEach(x -> {
                if (x.isType(t)) ret.set(x.convert(t));
            });

        return ret.get();
    }

    @Override
    public String JavaToDBValue(Object o) {
        if(o==null)
            return null;
        if(valueConverters.get(o.getClass())!=null)
            return valueConverters.get(o.getClass()).convertToDB(o);
        else {
            ISpecialValueConverter specialValueConverter= specialValueConverters.stream().filter(x->x.isType(o.getClass())).findFirst().orElse(null);
            if(specialValueConverter==null)
                return new DefaultConverter().convertToDB(o);
            else
                return specialValueConverter.convertToDB(o);
        }
    }

    @Override
    public Object DBToJavaValue(Object value, Class type) {
        if(valueConverters.get(type)!=null)
            return valueConverters.get(type).convertToJava(type,value);
        else {
            ISpecialValueConverter specialValueConverter= specialValueConverters.stream().filter(x->x.isType(type)).findFirst().orElse(null);
            if(specialValueConverter==null)
                return new DefaultConverter().convertToJava(type,value);
            else
                return specialValueConverter.convertToJava(type,value);
        }

    }


}
