package dsn.dev.java;

public class ZSemaphore {

	protected long _handle = 0;

	public ZSemaphore() 
	{
		Nativecalls.dsn_semaphore_create(0);
	}
	
	public ZSemaphore(int initial_count) 
	{
		Nativecalls.dsn_semaphore_create(initial_count);
	}
	
	protected boolean Release()
	{
		Nativecalls.dsn_semaphore_destroy(_handle);
	    return true;
	}

	public void signal() 
	{
		Nativecalls.dsn_semaphore_signal(_handle, 1); 
	}
	
	public void signal(int count) 
	{
		Nativecalls.dsn_semaphore_signal(_handle, count); 
	}
	
	public boolean wait(int timeout_milliseconds) 
	{
	    if (timeout_milliseconds == Integer.MAX_VALUE)
	    {
	    	Nativecalls.dsn_semaphore_wait(_handle);
	        return true;
	    }
	    else
	    {
	        return Nativecalls.dsn_semaphore_wait_timeout(_handle, timeout_milliseconds);
	    }
	
	}
}
