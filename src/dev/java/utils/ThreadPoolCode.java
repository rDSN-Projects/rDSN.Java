package dsn.dev.java.utils;
import dsn.dev.java.Nativecalls;

public class ThreadPoolCode {
	public static ThreadPoolCode THREAD_POOL_INVALID = new ThreadPoolCode("THREAD_POOL_INVALID");
    public static ThreadPoolCode THREAD_POOL_DEFAULT = new ThreadPoolCode("THREAD_POOL_DEFAULT");

    public ThreadPoolCode(int c)
    {
        _code = c;
    }

    public ThreadPoolCode(ThreadPoolCode c)
    {
        _code = c._code;
    }

    public ThreadPoolCode(String name)
    {
        _code = Nativecalls.dsn_threadpool_code_register(name);
    }

    public String ToString()
    {
        return Nativecalls.dsn_task_code_to_string(_code);
    }

    public int Code()
    {
        return _code;
    }
    
    public boolean Equals(Object obj)
    {
        return this._code == ((ThreadPoolCode)obj)._code;
    }
    
    private int _code;
}
