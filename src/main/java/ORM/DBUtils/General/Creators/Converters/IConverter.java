package ORM.DBUtils.General.Creators.Converters;

public interface IConverter {

    /** Converts Type t to column type
     *
     * @param t type
     * @return column type
     */
    String JavaToDBType(Class t);

    /** Converts java object to DB value
     *
     * @param o java object
     * @return DB value as String
     */
    String JavaToDBValue(Object o);

    /** Converts DB value to java object
     *
     * @param value DB value
     * @param type class in java
     * @return Java object
     */
    Object DBToJavaValue(Object value, Class type);

}
