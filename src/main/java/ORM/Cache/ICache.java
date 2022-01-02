package ORM.Cache;

import ORM.ORM;

public interface ICache {

    /**inits cache with orm
     *
     * @param orm
     */
    void init(ORM orm);

    /** get Object out of cache if it is contained
     *
     * @param t class of object
     * @param pk primary key of object
     * @return object for primary key
     */
    Object get(Class t, Object pk);

    /** Puts an object into the cache.
     * @param obj Object. */
    void put(Object obj);

    /** Removes entity from cache
     *
     * @param obj entity to remove from cache
     */
    void remove(Object obj);

    /** Checks if entity with primary key pk is contained
     *
     * @param t class of entity
     * @param pk primary key of entity
     * @return true if entity is contained otherwise false
     */
    boolean contains(Class t, Object pk);

    /** Checks if entity is contained
     *
     * @param obj
     * @return true if entity is contained otherwise false
     */
    boolean contains(Object obj);

    /** Gets if an object has changed.
     * @param obj Object.
     * @return true if the object has changed otherwise false */
    boolean hasChanged(Object obj);
}
