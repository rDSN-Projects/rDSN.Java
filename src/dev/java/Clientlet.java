package dsn.dev.java;

public class Clientlet
{
	protected long _handle = 0;
    
    public Clientlet()
    {
    	_handle = Nativecalls.dsn_task_tracker_create(13);
    }
    
    public Clientlet(int task_bucket_count)
    {
    	_handle = Nativecalls.dsn_task_tracker_create(task_bucket_count);
    }
    
    public boolean ReleaseHandle()
    {
        Nativecalls.dsn_task_tracker_destroy(_handle);
        return true;
    }

    protected long tracker() { return _handle; }

    public void wait_all_pending_tasks()
    {
    	Nativecalls.dsn_task_tracker_wait_all(_handle);
    }

    public void cancel_all_pending_tasks()
    {
    	Nativecalls.dsn_task_tracker_cancel_all(_handle);
    }
    
    public static RpcAddress primary_address() { return new RpcAddress(Nativecalls.dsn_primary_address()); }
    public static int random32(int min, int max) { return Nativecalls.dsn_random32(min, max); }
    public static long random64(long min, long max) { return Nativecalls.dsn_random64(min, max); }
    public static long now_ns() { return Nativecalls.dsn_now_ns(); }
    public static long now_us() { return Nativecalls.dsn_now_us(); }
    public static long now_ms() { return Nativecalls.dsn_now_ms(); }
    
    static void TaskCallback(int index)
    {
    	TaskCallbackHandler cb = (TaskCallbackHandler)GlobalInterOpLookupTable.GetRelease(index);
        cb.run();
    }

    static void TaskTimerCallback(int index)
    {
    	TaskCallbackHandler cb = (TaskCallbackHandler)GlobalInterOpLookupTable.Get(index);
        cb.run();
    }
    
    public static void CallAsync(
        TaskCode evt,
        Clientlet callbackOwner,
        TaskCallbackHandler callback,
        int hash,
        int delay_milliseconds,
        int timer_interval_milliseconds
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task;

        if (timer_interval_milliseconds == 0)
            task = Nativecalls.dsn_task_create(evt.Code(), index, hash);
        else
            task = Nativecalls.dsn_task_create_timer(evt.Code(), index, hash, timer_interval_milliseconds);

        Nativecalls.dsn_task_call(task, callbackOwner != null ? callbackOwner.tracker() : 0, delay_milliseconds);
    }
    
    public static TaskHandle CallAsync2(
        TaskCode evt,
        Clientlet callbackOwner,
        TaskCallbackHandler callback,
        int hash,
        int delay_milliseconds,
        int timer_interval_milliseconds
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task;

        if (timer_interval_milliseconds == 0)
            task = Nativecalls.dsn_task_create(evt.Code(), index, hash);
        else
            task = Nativecalls.dsn_task_create_timer(evt.Code(), index, hash, timer_interval_milliseconds);

        TaskHandle ret = new TaskHandle(task, index);
        Nativecalls.dsn_task_call(task, callbackOwner != null ? callbackOwner.tracker() : 0, delay_milliseconds);
        return ret;
    }
    
    public static TaskHandle CallAsync3(
        TaskCode evt,
        Clientlet callbackOwner,
        TaskCallbackHandler callback,
        int hash,
        int delay_milliseconds,
        int timer_interval_milliseconds
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task;

        if (timer_interval_milliseconds == 0)
            task = Nativecalls.dsn_task_create(evt.Code(), index, hash);
        else
            task = Nativecalls.dsn_task_create_timer(evt.Code(), index, hash, timer_interval_milliseconds);

        TaskHandle ret = new TaskHandle(task, index);
        Nativecalls.dsn_task_call(task, callbackOwner != null ? callbackOwner.tracker() : 0, delay_milliseconds);
        return ret;
    }
            
    // no callback
    public static void RpcCallOneWay(
        RpcAddress server,
        RpcWriteStream requestStream
        )
    {
        Logging.dassert(requestStream.IsFlushed(), "RpcWriteStream must be flushed after write in the same thread");
        Nativecalls.dsn_rpc_call_one_way(server.Addr(), requestStream.handle());
    }

    public static RpcReadStream RpcCallSync(
        RpcAddress server,
        RpcWriteStream requestStream
        )
    {
        Logging.dassert(requestStream.IsFlushed(), "RpcWriteStream must be flushed after write in the same thread");

        long respMsg = Nativecalls.dsn_rpc_call_wait(server.Addr(), requestStream.handle());
        if (respMsg == 0)
        {
            return null;
        }   
        else
        {
            return new RpcReadStream(respMsg, true);
        }
    }

    
    static void RpcResponseCallback(int err, long reqc, long respc, int index)
    {
    	RpcResponseCallbackHandler cb = (RpcResponseCallbackHandler)GlobalInterOpLookupTable.GetRelease(index);
        
        if (err == 0)
        {
        	RpcReadStream rms = new RpcReadStream(respc, false);
        	cb.run(new ErrorCode(err), rms);
        }
        else
        {
        	cb.run(new ErrorCode(err), null);
        }
    }

    public static void RpcCallAsync(
        RpcAddress server,
        RpcWriteStream requestStream,
        Clientlet callbackOwner,
        RpcResponseCallbackHandler callback,
        int replyHash
        )
    {
        Logging.dassert(requestStream.IsFlushed(), "RpcWriteStream must be flushed after write in the same thread");

        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_rpc_create_response_task(requestStream.handle(), index, replyHash);
        Nativecalls.dsn_rpc_call(server.Addr(), task, callbackOwner != null ? callbackOwner.tracker() : 0);
    }

    //
    // this gives you the task handle so you can wait or cancel
    // the task, with the cost of add/ref the task handle
    // 
    public static TaskHandle RpcCallAsync2(
        RpcAddress server,
        RpcWriteStream requestStream,
        Clientlet callbackOwner,
        RpcResponseCallbackHandler callback,
        int replyHash
        )
    {
        Logging.dassert(requestStream.IsFlushed(),
            "RpcWriteStream must be flushed after write in the same thread");

        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_rpc_create_response_task(requestStream.handle(), index, replyHash);

        TaskHandle ret = new TaskHandle(task, index);
        Nativecalls.dsn_rpc_call(server.Addr(), task, callbackOwner != null ? callbackOwner.tracker() : 0);
        return ret;
    }

    //----------------------AIO----------------------------------------
    
    public static long FileOpen(String file_name, int flag, int pmode)
    {
        return Nativecalls.dsn_file_open(file_name, flag, pmode);
    }

    public static ErrorCode FileClose(long file)
    {
        int err = Nativecalls.dsn_file_close(file);
        return new ErrorCode(err);
    }

    static void AioCallback(int err, int size, int index)
    {
    	AioCallbackHandler cb = (AioCallbackHandler)GlobalInterOpLookupTable.GetRelease(index);
        
    	cb.run(new ErrorCode(err), size);
    }

    public static long FileRead(
    	long hFile,
        byte[] buffer,
        int count,
        long offset,
        TaskCode callbackCode,
        Clientlet callbackOwner,
        AioCallbackHandler callback,
        int hash
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_file_create_aio_task(callbackCode.Code(), index, hash);
        Nativecalls.dsn_file_read(hFile, buffer, count, offset, task, callbackOwner != null ? callbackOwner.tracker() : 0);
        return task;
    }

    public static long FileWrite(
    	long hFile,
        byte[] buffer,
        int count,
        long offset,
        TaskCode callbackCode,
        Clientlet callbackOwner,
        AioCallbackHandler callback,
        int hash
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_file_create_aio_task(callbackCode.Code(), index, hash);
        Nativecalls.dsn_file_write(hFile, buffer, count, offset, task, callbackOwner != null ? callbackOwner.tracker() : 0);
        return task;
    }

    public static long CopyRemoteFiles(
    	long remote,
        String source_dir,
        String[] files,
        String dest_dir,
        boolean overwrite, 
        TaskCode callbackCode,
        Clientlet callbackOwner,
        AioCallbackHandler callback,
        int hash
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_file_create_aio_task(callbackCode.Code(), index, hash);
        Nativecalls.dsn_file_copy_remote_files(remote, source_dir, files, dest_dir, overwrite, task, callbackOwner != null ? callbackOwner.tracker() : 0);
        return task;
    }

    public static long CopyRemoteDirectory(
    	long remote,
        String source_dir,
        String dest_dir,
        boolean overwrite,
        TaskCode callbackCode,
        Clientlet callbackOwner,
        AioCallbackHandler callback,
        int hash
        )
    {
        int index = GlobalInterOpLookupTable.Put(callback);
        long task = Nativecalls.dsn_file_create_aio_task(callbackCode.Code(), index, hash);
        Nativecalls.dsn_file_copy_remote_directory(remote, source_dir, dest_dir, overwrite, task, callbackOwner != null ? callbackOwner.tracker() : 0);
        return task;
    }
}
