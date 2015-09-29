package dsn.dev.java;

public class ZLock {

	protected long _handle = 0;

	public ZLock() 
    {
		_handle = Nativecalls.dsn_exlock_create(false);
    }
	
	public ZLock(boolean recursive) 
    {
		_handle = Nativecalls.dsn_exlock_create(recursive);
    }

	protected boolean Release()
	{
		Nativecalls.dsn_exlock_destroy(_handle); 
		return true;
	}

	public void Lock() { Nativecalls.dsn_exlock_lock(_handle); }
	public void Unlock() { Nativecalls.dsn_exlock_unlock(_handle); }
	public boolean TryLock() { return Nativecalls.dsn_exlock_try_lock(_handle); }
}
