package dsn.dev.java;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InterOpLookupTable {
	public InterOpLookupTable(int init_count)
    {
        lock = new ReentrantLock();
        _objects = new ArrayList<Object>();
        _free_objects = new LinkedList<Integer>();
        for (int i = 0; i < _objects.size(); i++)
        {
            _objects.add(null);
            _free_objects.add(i);
        }
    }
        
	public int Put(Object obj)
    {
        int index;
        lock.lock();
        try
        {
            if (_free_objects.size() > 0)
            {
            	index = _free_objects.remove();
                _objects.set(index, obj);
            }
            else
            {
            	index = _objects.size();
                _objects.add(obj);                    
            }
            return index;
        }
    	finally
    	{
    		lock.unlock();
    	}
    }

    public Object Get(int index)
    {
    	Object obj;
    	lock.lock();
    	try
    	{
    		obj = _objects.get(index);
        	return obj;
    	}
    	finally
    	{
    		lock.unlock();
    	}
    }

    public Object GetRelease(int index)
    {
    	Object obj;
    	lock.lock();
        try
        {
            obj = _objects.get(index);
            _objects.set(index, null);
            _free_objects.add(index);
            return obj;
        }
        finally
        {
        	lock.unlock();
        }
    }

    private ArrayList<Object> _objects;
    private Queue<Integer> _free_objects;
    private Lock lock;
}
