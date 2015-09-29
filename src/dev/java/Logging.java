package dsn.dev.java;

public class Logging {
    public static void dlog(Nativecalls.dsn_log_level_t level, String fmt)
	{
	    Nativecalls.dsn_log("unknown", "unknown", 0, level, fmt);
	}

	public static void dlog(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
	{
		Nativecalls.dsn_log("unknown", "unknown", 0, level, String.format(fmt, arg0));
	}

	public static void dlog(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
	{
		Nativecalls.dsn_log("unknown", "unknown", 0, level, String.format(fmt, arg0, arg1));
	}

    public static void dlog(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
	{
    	Nativecalls.dsn_log("unknown", "unknown", 0, level, String.format(fmt, arg0, arg1, arg2));
	}


	public static void dinfo(Nativecalls.dsn_log_level_t level, String fmt)
	{
	    if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_INFORMATION) >= 0)
	    {
	                dlog(level, fmt);
	    }
	}

	public static void dinfo(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
	{
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_INFORMATION) >= 0)
	    {
	                dlog(level, fmt, arg0);
	    }
	}

	public static void dinfo(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
	{
	    if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_INFORMATION) >= 0)
	    {
	        dlog(level, fmt, arg0, arg1);
	    }
	}

	public static void dinfo(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
	{
	    if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_INFORMATION) >= 0)
	    {
	        dlog(level, fmt, arg0, arg1, arg2);
	    }
	}

	/// <summary>
	/// 
	/// </summary>
	/// <param name="?"></param>
	/// <param name="?"></param>
	public static void ddebug(Nativecalls.dsn_log_level_t level, String fmt)
	{
	    if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_DEBUG) >= 0)
	    {
	        dlog(level, fmt);
	    }
	}

	public static void ddebug(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
	{
	    if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_DEBUG) >= 0)
	    {
	        dlog(level, fmt, arg0);
	    }
	}

	public static void ddebug(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
	{
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_DEBUG) >= 0)
        {
            dlog(level, fmt, arg0, arg1);
        }
    }

    public static void ddebug(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_DEBUG) >= 0)
        {
            dlog(level, fmt, arg0, arg1, arg2);
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="?"></param>
    /// <param name="?"></param>
    /// 
    public static void dwarn(Nativecalls.dsn_log_level_t level, String fmt)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_WARNING) >= 0)
        {
            dlog(level, fmt);
        }
    }

    public static void dwarn(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_WARNING) >= 0)
        {
            dlog(level, fmt, arg0);
        }
    }

    public static void dwarn(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_WARNING) >= 0)
        {
            dlog(level, fmt, arg0, arg1);
        }
    }

    public static void dwarn(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_WARNING) >= 0)
        {
            dlog(level, fmt, arg0, arg1, arg2);
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="?"></param>
    /// <param name="?"></param>

    public static void derror(Nativecalls.dsn_log_level_t level, String fmt)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_ERROR) >= 0)
        {
            dlog(level, fmt);
        }
    }

    public static void derror(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_ERROR) >= 0)
        {
            dlog(level, fmt, arg0);
        }
    }

    public static void derror(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_ERROR) >= 0)
        {
            dlog(level, fmt, arg0, arg1);
        }
    }

    public static void derror(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_ERROR) >= 0)
        {
            dlog(level, fmt, arg0, arg1, arg2);
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="?"></param>
    /// <param name="?"></param>
    /// 
    public static void dfatal(Nativecalls.dsn_log_level_t level, String fmt)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL) >= 0)
        {
            dlog(level, fmt);
        }
    }

    public static void dfatal(Nativecalls.dsn_log_level_t level, String fmt, Object arg0)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL) >= 0)
        {
            dlog(level, fmt, arg0);
        }
    }

    public static void dfatal(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL) >= 0)
        {
            dlog(level, fmt, arg0, arg1);
        }
    }

    public static void dfatal(Nativecalls.dsn_log_level_t level, String fmt, Object arg0, Object arg1, Object arg2)
    {
        if (Nativecalls.dsn_log_get_start_level().compareTo(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL) >= 0)
        {
            dlog(level, fmt, arg0, arg1, arg2);
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="?"></param>
    /// <param name="?"></param>
    public static void dassert(boolean condition, String fmt)
    {
        if (!condition)
        {
            dlog(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL, fmt);
            Nativecalls.dsn_coredump();
        }
    }

    public static void dassert(boolean condition, String fmt, Object arg0)
    {
        if (!condition)
        {
            dlog(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL, fmt, arg0);
            Nativecalls.dsn_coredump();
        }
    }

    public static void dassert(boolean condition, String fmt, Object arg0, Object arg1)
    {
        if (!condition)
        {
            dlog(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL, fmt, arg0, arg1);
            Nativecalls.dsn_coredump();
        }
    }

    public static void dassert(boolean condition, String fmt, Object arg0, Object arg1, Object arg2)
    {
        if (!condition)
        {
            dlog(Nativecalls.dsn_log_level_t.LOG_LEVEL_FATAL, fmt, arg0, arg1, arg2);
            Nativecalls.dsn_coredump();
        }
    }
}
