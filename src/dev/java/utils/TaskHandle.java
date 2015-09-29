package dsn.dev.java.utils;
import dsn.dev.java.Nativecalls;
import dsn.dev.java.GlobalInterOpLookupTable;

public class TaskHandle {
	
	long _handle;
	int _callback_index = -1;
	
	public TaskHandle(long nativeHandle, int callback_index)
	{
		_handle = nativeHandle;
		Nativecalls.dsn_task_add_ref(nativeHandle);
	}
	
	protected boolean ReleaseHandle()
	{
		Nativecalls.dsn_task_release_ref(_handle);
	    return true;
	}
	
	public boolean Cancel(boolean waitFinished)
	{
		if (Nativecalls.dsn_task_cancel(_handle, waitFinished))
		{
			GlobalInterOpLookupTable.GetRelease(_callback_index);
			return true;
		}
		else
			return false;
	}
	
	public boolean Cancel(boolean waitFinished, boolean finished[])
	{
		if (Nativecalls.dsn_task_cancel2(_handle, waitFinished, finished))
        {
            GlobalInterOpLookupTable.GetRelease(_callback_index);
            return true;
        }
        else
            return false;
	}
	
	public void Wait()
	{
	    Nativecalls.dsn_task_wait(_handle);
	}
	
	public boolean WaitTimeout(int milliseconds)
	{
	    return Nativecalls.dsn_task_wait_timeout(_handle, milliseconds);
	}
}
