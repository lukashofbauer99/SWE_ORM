package ORM.DBUtils.General.Creators.Converters;

public interface ISpecialValueConverter {

    /** checks if t is special type
     *
     * @param t type to check
     * @return true if is special type, false if is not
     */
    boolean isType(Class t);

    /** converts special object to DB value
     *
     * @param value class of special type
     * @return DB value
     */
    String convertToDB(Object value);

    /** converts DB value to special object
     *
     * @param c class of special type
     * @param value DB value as Object
     * @return special object
     */
    Object convertToJava(Class c,Object value);
}
