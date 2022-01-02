package ORM.DBUtils.General.Manipulators.Queries;

import ORM.ORM;

import java.util.Collection;

public interface IQueryStringBuilder<T> {

    /** fills fields of this object
     *
     * @param c generic
     * @param orm orm
     * @param s string
     * @param prev previous query string builder
     * @return this query string builder
     */
    IQueryStringBuilder fillQueryStringBuilder(Class<T> c,ORM orm, String s,IQueryStringBuilder prev);
    IQueryStringBuilder fillQueryStringBuilder(Class<T> c,ORM orm);


    /** adds where and the column name of the field to the query string
     *
     * @param fieldName field name for which the column name is added
     * @return new query string builder
     */
    IQueryStringBuilder where(String fieldName);

    /** adds and and the column name of the field to the query string
     *
     * @param fieldName field name for which the column name is added
     * @return new query string builder
     */
    IQueryStringBuilder and(String fieldName);

    /** adds or and the column name of the field to the query string
     *
     * @param fieldName field name for which the column name is added
     * @return new query string builder
     */
    IQueryStringBuilder or(String fieldName);

    /** adds constant value to the query string
     *
     * @param val constant value which is added
     * @return new query string builder
     */
    IQueryStringBuilder constVal(Object val);

    /** adds column name to the query string
     *
     * @param fieldName field name for which the column name is added
     * @return new query string builder
     */
    IQueryStringBuilder columnVal(String fieldName);

    /** adds comparer to the query string
     *
     * @param comparer comparer which  is added
     * @return new query string builder
     */
    IQueryStringBuilder comparer(String comparer);

    /** calls build of the previous query string builder and adds own query string to that
     *
     * @return addition of return value of previous query builder build and own query string
     */
    String build();

    /** executes Select with built where condition
     *
     * @return collection of entities of type T that match that condition
     */
    Collection<T> execute();

}
