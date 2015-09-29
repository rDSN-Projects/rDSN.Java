package dsn.dev.java;

public class ZRwLockNr {
	
	protected long _handle = 0;
	
	public ZRwLockNr()
	{
		_handle = Nativecalls.dsn_rwlock_nr_create();
	}

	protected boolean Release()
	{
	    Nativecalls.dsn_rwlock_nr_destroy(_handle);
	    return true;
	}
	
	public void LockRead() { Nativecalls.dsn_rwlock_nr_lock_read(_handle);  }
	public void UnlockRead() { Nativecalls.dsn_rwlock_nr_unlock_read(_handle); }
	
	public void LockWrite() { Nativecalls.dsn_rwlock_nr_lock_write(_handle); }
	public void UnlockWrite() { Nativecalls.dsn_rwlock_nr_unlock_write(_handle); }
}
