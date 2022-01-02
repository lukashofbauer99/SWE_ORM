package ORM.Cache;

import ORM.ORM;

import java.util.HashMap;

public class BaseCache implements ICache{

    private HashMap<Class, HashMap<Object, Object>> _caches = new HashMap<>();

    private ORM orm;


    /** Gets Cache for Class
     *
     * @param t class
     * @return Cache for class
     */
    private HashMap<Object, Object> getCache(Class t)
    {
        if(_caches.containsKey(t)) { return _caches.get(t); }

        HashMap<Object, Object> returnVal = new HashMap<>();
        _caches.put(t, returnVal);

        return returnVal;
    }


    @Override
    public void init(ORM orm) {
        this.orm=orm;
    }


    @Override
    public Object get(Class t, Object pk)
    {
        HashMap<Object, Object> c = getCache(t);

        if(c.containsKey(pk)) { return c.get(pk); }
        return null;
    }


    @Override
    public void put(Object obj)
    {
        if(obj != null) { getCache(obj.getClass()).put(orm.getID(obj), obj); }
    }


    @Override
    public void remove(Object obj)
    {
        getCache(obj.getClass()).remove(orm.getID(obj));
    }


    @Override
    public boolean contains(Class t, Object pk)
    {
        return getCache(t).containsKey(pk);
    }


    @Override
    public boolean contains(Object obj)
    {
        return getCache(obj.getClass()).containsKey(orm.getID(obj));
    }


    @Override
    public boolean hasChanged(Object obj)
    {
        return true;
    }
}
