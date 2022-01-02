package ORM.DBUtils.General.Creators.Converters;

public interface IValueConverter {


    /** convertsjava object to DB value
     *
     * @param value class of java object
     * @return DB value
     */
    String convertToDB(Object value);

    /** converts DB value to java object
     *
     * @param c class of java object
     * @param value DB value as Object
     * @return java object
     */
    Object convertToJava(Class c,Object value);

}
