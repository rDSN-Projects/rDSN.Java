package dsn.dev.java;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;


import dsn.dev.java.utils.*;

public class Serverlet extends Clientlet
{
	public Serverlet(String name, int task_bucket_count)
    {
        super(task_bucket_count);
        _name = name;
        lock = new ReentrantLock();
        handlers = new ConcurrentHashMap<TaskCode, RpcRequestCallbackHandler>();
    }

	public void Unregister()
	{
		Iterator<Entry<TaskCode, RpcRequestCallbackHandler>> iter = handlers.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<TaskCode, RpcRequestCallbackHandler> entry = iter.next();
			UnregisterRpcHandler(entry.getKey());
	    }   
	}

	public static void RpcRequestCallback(long req, int index)
	{
		RpcRequestCallbackHandler cb = (RpcRequestCallbackHandler) GlobalInterOpLookupTable.Get(index);
		cb.callback(req);
	}
	
	protected boolean RegisterRpcHandler(TaskCode code, String name, RpcRequestHandler handler)
	{
		RpcRequestCallbackHandler cb = new RpcRequestCallbackHandler(handler);
		int index = GlobalInterOpLookupTable.Put(cb);
	    boolean r = Nativecalls.dsn_rpc_register_handler(code.Code(), name, index);
	    Logging.dassert(r, "rpc handler registration failed for " + code.ToString());
	
	    lock.lock();
	    try
	    {
	    	handlers.put(code, cb);
	    }
	    finally
	    {
	    	lock.unlock();
	    }
	    return r;
	}
	
	protected boolean RegisterRpcHandler(TaskCode code, String name, RpcRequestHandlerOneWay handler)
	{
		RpcRequestCallbackHandler cb = new RpcRequestCallbackHandler(handler);
		int index = GlobalInterOpLookupTable.Put(cb);
	    boolean r = Nativecalls.dsn_rpc_register_handler(code.Code(), name, index);
	    Logging.dassert(r, "rpc handler registration failed for " + code.ToString());
	
	    lock.lock();
	    try
	    {
	    	handlers.put(code, cb);
	    }
	    finally
	    {
	    	lock.unlock();
	    }
	    return r;
	}

	protected boolean UnregisterRpcHandler(TaskCode code)
    {
		Nativecalls.dsn_rpc_unregiser_handler(code.Code());
        return true;
    }
	
	protected void Reply(RpcWriteStream response)
	{
	    Logging.dassert(response.IsFlushed(), "RpcWriteStream must be flushed after write in the same thread");
	    Nativecalls.dsn_rpc_reply(response.handle());
	}

    public String Name() { return _name; }

    private String _name;
    private ConcurrentHashMap<TaskCode, RpcRequestCallbackHandler> handlers;
    private Lock lock;
}
