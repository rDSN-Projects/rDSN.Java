package dsn.dev.java.utils;
import dsn.dev.java.Nativecalls;

public class TaskCode {
	public static TaskCode TASK_CODE_INVALID = new TaskCode("TASK_CODE_INVALID", Nativecalls.dsn_task_type_t.TASK_TYPE_COMPUTE, Nativecalls.dsn_task_priority_t.TASK_PRIORITY_COMMON, ThreadPoolCode.THREAD_POOL_DEFAULT);

    public TaskCode(int c)
    {
        _code = c;
    }

    public TaskCode(TaskCode c)
    {
        _code = c._code;
    }

    public TaskCode(String name, Nativecalls.dsn_task_type_t type, Nativecalls.dsn_task_priority_t pri, ThreadPoolCode pool)
    {
        _code = Nativecalls.dsn_task_code_register(name, type, pri, pool.Code());
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
        return this._code == ((TaskCode)obj)._code;
    }

    private int _code;
}
