package ORM.DBUtils.General.Creators.Converters;

public interface ISpecialTypeConverter {

    /** checks if t is special type
     *
     * @param t type to check
     * @return true if is special type, false if is not
     */
    boolean isType(Class t);

    /** converts special type to DB type
     *
     * @param t class of special type
     * @return DB type
     */
    String convert(Class t);

}
